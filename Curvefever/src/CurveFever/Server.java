package CurveFever;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Client {

    private static final int timerInterval = ServerSidePlayer.SYSTEM_TICK;
    private Game game;
    private Timer gameTimer;
    private int numOfClients;   /* NOT the number of players!!!!!!!! */
    private int numOfRounds;
    private ServerSocket ss;
    private Socket[] Sockets;
    private ObjectOutputStream[] ObjOutStreams;
    private ObjectInputStream[] ObjInStreams;
    private GetControl[] GCRunnables;
    private SendPoints[] SPRunnables;
    private Thread[] GCThreads;
    private Thread[] SPThreads;
    private PackageS2C currentPkg;
    private ControlState[] ControlStates;
    private int cycleCounter;
    private boolean waitWithDraw = true;
    private int prevGameRound;
    private int prepStartTime;


    public Server(int numOfPlayers, int numOfRounds, String playerName) {
        super("", playerName);


        this.numOfClients = numOfPlayers - 1;
        this.numOfRounds = numOfRounds;
        this.Sockets = new Socket[numOfClients];
        this.ObjInStreams = new ObjectInputStream[numOfClients];
        this.ObjOutStreams = new ObjectOutputStream[numOfClients];
        this.GCRunnables = new GetControl[numOfClients];
        this.SPRunnables = new SendPoints[numOfClients];
        this.GCThreads = new Thread[numOfClients];
        this.SPThreads = new Thread[numOfClients];

        this.ControlStates = new ControlState[numOfPlayers];


        try {
            ss = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("IOException from server constructor");
        }
        System.out.println("----- Server created -----");
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getPort() {
        return port;
    }

    /* used for signaling to menu class */
    public boolean waitForDraw() {
        return waitWithDraw;
    }

    /* used for signaling to menu class */
    public void drawFinished() {
        waitWithDraw = true;
    }

    /* this functions waits for the specified number of clients to join.
    * It stores the socket and IO streams for each client.
    * It also creates the runnable objects */
    public void acceptConnections() {
        int numOfPlayer = 0;

        System.out.println("Waiting for connections.... (1/" + (numOfClients + 1) + ")");
        try {
            while (numOfPlayer < numOfClients) {
                /* accept new connection */
                Sockets[numOfPlayer] = ss.accept();

                /* store IO streams, and create runnables */
                ObjOutStreams[numOfPlayer] = new ObjectOutputStream(Sockets[numOfPlayer].getOutputStream());
                ObjInStreams[numOfPlayer] = new ObjectInputStream(Sockets[numOfPlayer].getInputStream());

                SPRunnables[numOfPlayer] = new SendPoints(numOfPlayer);
                GCRunnables[numOfPlayer] = new GetControl(numOfPlayer);

                /* increment player number */
                numOfPlayer++;
                System.out.println("New player connected. " + (numOfPlayer + 1) + "/" + (numOfClients + 1));
            }
            System.out.println("All " + (numOfClients + 1) + " players connected");
        } catch (IOException ex) {
            System.out.println("IOException from acceptConnections()");
        }

    }

    /* closes all sockets and IO streams with the clients */
    public void closeAllConnections(){
        for (int i = 0; i < numOfClients; i++) {
            try {
                Sockets[i].close();
                ObjOutStreams[i].close();
                ObjInStreams[i].close();

            } catch (IOException e) {
                System.out.println("IOexception, when trying to close socket #" + i);
            }

        }
        try {
            ss.close();
        } catch (IOException e) {
            System.out.println("IOexception, when trying to close server socket ");
        }
    }

    /* sets up the game for all clients. This includes collecting all player names
    *  randomly assigning  colors, and the first starting position for each player
    *  Note: the server is a "special client", so it also accommodates a player */
    public void setupGame() {

        /* Create init package */
        InitPackageS2C pkg = new InitPackageS2C(numOfClients + 1);
        pkg.gameState = GameState.MENU;
        pkg.numOfRounds = this.numOfRounds;


        /* get all the player names */
        for (int i = 0; i < numOfClients; i++) {
            try {
                /* send request */
                ObjOutStreams[i].writeUTF("Please send player name!");
                ObjOutStreams[i].flush();

                /* wait for response */
                pkg.playerNames[i] = ObjInStreams[i].readUTF();
            } catch (IOException e) {
                System.out.println("IOException from setupGame, while requesting names");
            }

            /* add the local player's name */
            pkg.playerNames[numOfClients] = this.player.getName();
        }

        /* create the players that the server uses for calculations*/
        ServerSidePlayer[] SSPlayers = new ServerSidePlayer[numOfClients + 1];

        /* add client players, and local player */
        for (int idx = 0; idx < (numOfClients + 1); idx++) {
            SSPlayers[idx] = new ServerSidePlayer(pkg.playerNames[idx], idx);
        }

        /* create the game instance. For reference tha game Class contains the game logic */
        game = new Game(numOfClients + 1, pkg.numOfRounds, SSPlayers, pkg.Colors);
        game.initGame();
        prevGameRound = game.getCurrentRound();

        /* get init data from the game */
        pkg.CurvePoints = game.getMainBoard().getLastCurvePoints();
        pkg.Colors = game.getColors();
        pkg.currentRound = game.getCurrentRound();

        /* send out init packages for all players */
        for (int i = 0; i < numOfClients; i++) {
            /* update package */
            pkg.Scores[i] = 0;
            pkg.playerID = i;

            /* send package */
            try {
                /* send package */
                ObjOutStreams[i].writeObject(pkg);
                ObjOutStreams[i].flush();

            } catch (IOException e) {
                System.out.println("IOException from setupGame, while sending init packages");
            }
        }

        /* set up cycle counter. The game is tick based */
        cycleCounter = 0;

        /* the first round will start with prep */
        game.setGameState(GameState.PREP);
        prepStartTime = cycleCounter;

        /*set up the main game timer*/
        setUpTimer();

        /* start timer */
        gameTimer.start();
    }

    /* configures the gametimer */
    public void setUpTimer() {

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* call server.run(), the main function */
                runServer();

            }
        };
        gameTimer = new Timer(timerInterval, al);
    }

    /* This function starts the threads that send the newly calculated game data to clients */
    public void sendToClient(PackageS2C pkg) {

        /* save data to a member variable, so all runnables can access it */
        currentPkg = pkg;

        /* create and start data sending threads */
        for (int idx = 0; idx < numOfClients; idx++) {
            SPThreads[idx] = new Thread(SPRunnables[idx]);
            SPThreads[idx].start();
        }

        /* wait for them to finnish */
        for (int idx = 0; idx < numOfClients; idx++) {
            try {
                SPThreads[idx].join();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException when waiting for SPThread #" + idx + " to die");
            }
        }

    }

   /* this function sends out the end of game message for all clients */
    public void sendEndOfGameToClients() {

        /* send out a request */
        for (int clientId = 0; clientId < numOfClients; clientId++) {
            try {
                ObjOutStreams[clientId].writeUTF("Game over");
                ObjOutStreams[clientId].flush();
            } catch (IOException e) {
                System.out.println("IOException when trying to send end of game message to client: #" + clientId);
            }
        }

    }

    /* this function waits for the clients to indicate that they want a rematch */
    public void waitForReplayMsgs() {

        WaitForStringFromClientRunnable[] runnables= new WaitForStringFromClientRunnable[numOfClients];
        Thread[] threads = new Thread[numOfClients];
        for (int i = 0; i < numOfClients; i++) {
            runnables[i] = new WaitForStringFromClientRunnable(i);

            threads[i] = new Thread(runnables[i]);
            threads[i].start();
        }

        for (int i = 0; i < numOfClients; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException from while waiting for Replay message #" + i);
            }
        }

        for (int clientId = 0; clientId < numOfClients; clientId++) {
            try {
                ObjOutStreams[clientId].writeUTF("Replay ok, start again!");
                ObjOutStreams[clientId].flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* runnable object for waiting for rematch messages */
    private class  WaitForStringFromClientRunnable implements Runnable {

        private int clientId;

        public WaitForStringFromClientRunnable(int i) {
            this.clientId = i;
        }

        public void run(){
            try {
                ObjInStreams[clientId].readUTF();
            } catch (IOException e) {
                System.out.println("IOException in waitForStringFromClient");
            }
        }
    }

    /* This function starts the threads that request control input data from clients */
    public void requestInputs() {
        /* Create and start threads, to get input of clients */
        for (int idx = 0; idx < numOfClients; idx++) {
            GCThreads[idx] = new Thread(GCRunnables[idx]);
            GCThreads[idx].start();
        }

        /* wait for all thread to finnish */
        for (int idx = 0; idx < numOfClients; idx++) {
            try {
                GCThreads[idx].join();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException from GCThread #" + idx);
            }
        }

        /* get the local player's input */
        ControlStates[numOfClients] = player.getControlState();
    }

    /* server main function. This function controls the playing aspect of the game.
    * This is called cyclically by the timer. */
    public void runServer() {
        /* request control inputs from all clients */
        requestInputs();

        //TODO Balazs itt folytasd a cleanup-ot
        boolean endRound;
        endRound = game.runGame(ControlStates, cycleCounter);

        if (endRound) {
            /* increment the round number */
            game.incrCurrRound();

            /* end of game, stop the counter */
            if (game.getMainBoard().getCurrentRound() > game.getRoundNum()) {
                gameTimer.stop();
                game.updatePlayerScores();
                game.setGameState(GameState.MENU);
            }

            /* prepare game for the next round */
            else {
                game.updatePlayerScores();
                game.getMainBoard().clearBoard();
                game.initPositions();
                game.initBoard();
                game.setGameState(GameState.PREP);
                game.setAllPlayersAlive();

                prepStartTime = cycleCounter;
            }
        }

        PackageS2C pkg = new PackageS2C(numOfClients + 1);

        if (game.getGameState() == GameState.PREP) {
            // Set prep speeds from the game board
            Vector2D[] prepSpeeds = new Vector2D[game.getPlayerNum()];
            for (int i = 0; i < game.getPlayerNum(); i++) {
                prepSpeeds[i] = new Vector2D(game.getMainBoard().getCurves()[i].getLastPoint().getX(),
                        game.getMainBoard().getCurves()[i].getLastPoint().getY());
            }
            pkg.prepSpeed = prepSpeeds;

            if ((cycleCounter - prepStartTime) >= ServerSidePlayer.PREP_TIME) {
                game.setGameState(GameState.PLAYING);
                game.getMainBoard().deleteLastCurvePoints();
            }

            /* last one will be the prepSpeed so we need the one before that */
            pkg.CurvePoints = game.getMainBoard().getLastLastCurvePoints();
        } else if (game.getGameState() == GameState.PLAYING) {
            pkg.CurvePoints = game.getMainBoard().getLastCurvePoints();
        }


        pkg.currentRound = game.getCurrentRound();


        pkg.Scores = game.getScores();


        pkg.gameState = game.getGameState();
        //pkg.gameState = GameState.PLAYING;
        cycleCounter++;
        sendToClient(pkg);

        waitWithDraw = false;
        System.out.println("runServer set waitForDraw");

    }

    /* private helper classes */
    private class GetControl implements Runnable {

        private final int clientId;

        public GetControl(int clientId) {

            this.clientId = clientId;
            System.out.println("GetControl runnable for client #" + clientId + "created");
        }

        public void run() {

            try {
                /* send out a request */
                ObjOutStreams[clientId].writeUTF("Provide control input pls");
                ObjOutStreams[clientId].flush();

                /* wait for the client to reply */
                ControlStates[clientId] = (ControlState) ObjInStreams[clientId].readObject();
            } catch (IOException e) {
                System.out.println("IOException from getControl runnable #" + clientId);
            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException from getControl runnable #" + clientId);
            }
        }
    }

    private class SendPoints implements Runnable {

        private final int clientId;

        public SendPoints(int clientId) {
            this.clientId = clientId;
            System.out.println("SendPoints runnable for client #" + clientId + "created");
        }

        public void run() {

            try {
                ObjOutStreams[clientId].writeObject(currentPkg);
            } catch (IOException e) {
                System.out.println("IOException from SendPoints runnable #" + clientId);
            }
        }
    }
}
