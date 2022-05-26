package CurveFever;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.Timer;


public class Server extends Client{

    private Game game;
    private Timer gameTimer;
    private int numOfClients;   //NOT the number of players!!!!!!!!
    private int numOfRounds;
    private ServerSocket ss;
    private Socket[] Sockets;
    private ObjectOutputStream[] ObjOutStreams;
    private ObjectInputStream[]  ObjInStreams;
    private GetControl[] GCRunnables;
    private SendPoints[] SPRunnables;
    private Thread[] GCThreads;
    private Thread[] SPThreads;
    private PackageS2C currentPkg;

    private ControlState[] ControlStates;

    private static final int timerInterval = ServerSidePlayer.SYSTEM_TICK;
    private int cycleCounter;
    private boolean waitWithDraw = true;


    public Server(int numOfPlayers,int numOfRounds, String playerName) {
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

    //not sure if this is needed, or correct
    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public int getPort() {
        return port;
    }

    /* used for signaling to menu class */
    public boolean waitForDraw() {
        return waitWithDraw;
    }

    /* used for signaling to menu class */
    public void drawFinished(){
        waitWithDraw = true;
    }

    public void acceptConnections()
    {
        int numOfPlayer = 0;

        System.out.println("Waiting for connections.... (1/"+(numOfClients+1)+")");
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
                System.out.println("New player connected. "  + (numOfPlayer+1)+ "/" + (numOfClients+1)) ;
            }
            System.out.println("All " + (numOfClients + 1) + " players connected");
        } catch (IOException ex) {
            System.out.println("IOException from acceptConnections()");
        }

    }

    public void setupGame(){

        /* Create init package */
        InitPackageS2C pkg = new InitPackageS2C(numOfClients+1);
        pkg.currentRound = 0;
        pkg.gameState = GameState.MENU; //TODO (M) ez igy ok Marci? aka.: mi legyen a gameState-el
        pkg.CurvePoints = null;
        pkg.numOfRounds = this.numOfRounds;


        /* get all the player names */
        for (int i = 0; i < numOfClients; i++) {
            try {
                /* send request */
                ObjOutStreams[i].writeUTF("Please send player name!");
                ObjOutStreams[i].flush();

                /* wait for response */
                pkg.playerNames[i] =  ObjInStreams[i].readUTF();
            } catch (IOException e) {
               System.out.println("IOException from setupGame, while requesting names");
            }

            /* add the local player's name */
            pkg.playerNames[numOfClients] = this.player.getName();
        }

        /* generate random colors for the players */
        //TODO (M/B) Marci random szingeneralojat illeszteni, ezt a borzalmat meg torolni
        //pkg.Colors[0] = new java.awt.Color(255,105,180);
        //pkg.Colors[1] = new java.awt.Color(124,255,255);
        //if (numOfClients+1 > 2) {
        //    pkg.Colors[2] = new java.awt.Color(255,0,0);
        //}
        //if (numOfClients+1 > 3) {
        //   pkg.Colors[3] = new java.awt.Color(0,255,0);
        //}
        //****** idaig kell majd torolni



        ServerSidePlayer[] SSPlayers= new ServerSidePlayer[numOfClients+1];

        /* add client players, and local player */
        for (int idx = 0; idx < (numOfClients+1); idx++) {
            SSPlayers[idx] = new ServerSidePlayer(pkg.playerNames[idx],idx);
        }

        game = new Game(numOfClients+1, pkg.numOfRounds, SSPlayers,pkg.Colors);
        game.initGame();
        //TODO (M) init game
        //TODO (B) setup server. Nem tudom mire gondoltam pontosan

        pkg.Colors = game.getColors();
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
        gameTimer = new Timer(timerInterval,al);
    }




    public void sendToClient(PackageS2C pkg) {

        /* save data to a member variable, so all runnables can acces it */
        currentPkg = pkg;

        /* create and start data sending threads */
        for (int idx = 0; idx < numOfClients; idx++) {
            SPThreads[idx] = new Thread(SPRunnables [idx]);
            SPThreads[idx].start();
        }

        /* wait for them to finnish */
        for (int idx = 0; idx < numOfClients; idx++) {
            try {
                SPThreads[idx].join();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException when waiting for SPThread #"+idx+" to die");
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
                System.out.println("InterruptedException from GCThread #"+idx);
            }
        }

        /* get the local player's input */
        ControlStates[numOfClients] = player.getControlState();

        //debug
        //System.out.println("Input client[0]:" + ControlStates[0] + "   Input client[1]: " + ControlStates[1]);

    }

    public void runServer() {
        requestInputs();

        // TODO (M/B) call game main function, to calculate everything
        /* test code, not final -------------------------------------------------  */
        game.updatePositions(ControlStates);
        /* end of test code ---------------------------------------------------------- */

        PackageS2C pkg = new PackageS2C(numOfClients+1);
        pkg.CurvePoints = game.getMainBoard().getLastCurvePoints();
        pkg.currentRound = game.getCurrentRound();


        //pkg.Scores = game.getScores(); //commeented out to test score sorrend xd
        pkg.Scores = new double[]{1,5};

        pkg.gameState = game.getGameState();
        cycleCounter++;
        sendToClient(pkg);

        if (cycleCounter >= 2500){
            gameTimer.stop();
        }
        System.out.print("cycle count: " + cycleCounter + " ");

        waitWithDraw = false;

    }

    /* private helper classes */
    private class GetControl implements Runnable {

        private final int clientId;

        public GetControl(int clientId){

            this.clientId = clientId;
            System.out.println("GetControl runnable for client #" + clientId + "created");
        }
        public void run() {

            try {
                /* send out a request */
                ObjOutStreams[clientId].writeUTF("Provide control input pls");
                ObjOutStreams[clientId].flush();

                /* wait for the client to reply */
                ControlStates[clientId] = (ControlState)ObjInStreams[clientId].readObject();
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
