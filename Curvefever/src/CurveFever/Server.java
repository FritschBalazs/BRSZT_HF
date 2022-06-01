package CurveFever;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;


public class Server extends Client {

    private static final int timerInterval = ServerSidePlayer.SYSTEM_TICK;
    private Game game;
    private Timer gameTimer;
    private int numOfClients;   //NOT the number of players!!!!!!!!
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

    //not sure if this is needed, or correct
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


        ServerSidePlayer[] SSPlayers = new ServerSidePlayer[numOfClients + 1];

        /* add client players, and local player */
        for (int idx = 0; idx < (numOfClients + 1); idx++) {
            SSPlayers[idx] = new ServerSidePlayer(pkg.playerNames[idx], idx);
        }

        game = new Game(numOfClients + 1, pkg.numOfRounds, SSPlayers, pkg.Colors);
        game.initGame();
        prevGameRound = game.getCurrentRound();


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


        cycleCounter = 0;
        game.setGameState(GameState.PREP);
        prepStartTime = cycleCounter;

        /*setup timer*/
        setUpTimer();

        /* start timer */
        gameTimer.start();
    }


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


    public void sendToClient(PackageS2C pkg) {

        /* save data to a member variable, so all runnables can acces it */
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

        //debug
        //System.out.println("Input client[0]:" + ControlStates[0] + "   Input client[1]: " + ControlStates[1]);

    }

    public void runServer() {
        /* request control inputs from all clients */
        requestInputs();

        boolean endRound;

        endRound = game.runGame(ControlStates, cycleCounter);   // TODO (B/M) remove cycleCounter - nem kell, végül random lyukakhoz kelleni fog


        if (endRound) {
            /* increment the round number */
            game.incrCurrRound();

            /* end of game, stop the counter */
            if (game.getMainBoard().getCurrentRound() > game.getRoundNum()) {
                gameTimer.stop();
                game.updatePlayerScores();
                game.setGameState(GameState.MENU);
                //TODO (B) ujrakezdes
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
