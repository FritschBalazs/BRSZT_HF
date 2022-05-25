package CurveFever;

import CurveFever.gui.GameScreen;
import CurveFever.gui.ScreenManager;

import javax.swing.*;

public class Menu {
    private Client client;
    private Server server; //only one Server or one Client will be used
    //private ProgramState programState;
    private ControlOption controlOptions;
    private String serverIp; //To give to Server or Client constructor
    private String playerName; //To give to Player constructor (player name can be entered in menu, in a textbox)
    //private int numOfPlayers;
    //private int numOfRounds;
    //TODO ha nagyjabol kesz a menu osztaly akkor ezt purgalni

    private ScreenManager sManager;

    public Menu(){}

    //public void setProgramState(ProgramState programState) {this.programState = programState;}
    public void setControlOptions(ControlOption controlOptions) {this.controlOptions = controlOptions;}
    public void setServerIp(String serverIp) {this.serverIp = serverIp;}
    public void setPlayerName(String playerName) {this.playerName = playerName;}
    //public void setNumOfPlayers(int numOfPlayers) {this.numOfPlayers = numOfPlayers;}
    //public void setNumOfRounds(int numOfRounds) {this.numOfRounds = numOfRounds;}
    public Client getClient() {return client;}
    public Server getServer() {return server;}
    //public ProgramState getProgramStateState() {return programState;}
    public ControlOption getControlOptions() {return controlOptions;}
    public String getServerIp() {return serverIp;}
    public String getPlayerName() {return playerName;}
    //public int getNumOfPlayers() {return numOfPlayers;}
    //public int getNumOfRounds() {return numOfRounds;}


    public void createGame(){
        //Start game in Server mode
    }

    public void joinGame(){
        //Start game in Client mode
    }

    public void updateMenu(){
        //Controlling menu logic (big while loop)
    }

    public void renderMenu(){
        //Drawing Menu
    }

    public void runMenu(){
        JFrame window = new JFrame("Kurve Fívör gui");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ScreenManager screenManager = new ScreenManager();

        window.add(screenManager);
        window.pack();
        window.setVisible(true);


        /* wait for the player to start click create game or joing game */
        while (screenManager.getProgramState() == ProgramState.MAIN_MENU) {
            screenManager.update();
        }

        /* if this instance is a server, create the server, and start the game */
        if (screenManager.isServer() == true){



            /* create server */
            int numOfPlayers = screenManager.getNumOfPlayers();
            int numOfRounds = screenManager.getNumOfRounds();
            String serverPlayerName = screenManager.getPlayerName();
            this.server = new Server(numOfPlayers,numOfRounds,serverPlayerName);
            this.client = null;

            window.setTitle("Kurve Fívör gui (szerver, player: " + serverPlayerName + ")");

            /* create game screen and add it to screenManager */ //servernel lehetne a scrrenmanager-en belulre, kliensnel nehezebb
            GameScreen gameScr = new GameScreen(numOfPlayers);
            screenManager.setGameScreen(gameScr);



            /* setup connections and configure game */
            this.server.acceptConnections();
            this.server.setupGame();

            /* eneter game loop */
            while (screenManager.getProgramState() == ProgramState.IN_GAME) {
                Board boardToDisplay = this.server.getGame().getMainBoard(); //todo ezt optimalizalni
                screenManager.getGameScreen().setCurves(boardToDisplay.getCurves());
                screenManager.getGameScreen().setCurrentRound(boardToDisplay.getCurrentRound());
                screenManager.getGameScreen().setPlayerNames(boardToDisplay.getPlayerNames());
                screenManager.getGameScreen().setScores(boardToDisplay.getScores());
                screenManager.getGameScreen().setRoundNum(boardToDisplay.getRoundNum());

                screenManager.update();
            }
        }

        /* if this insatance is a client, setup the client, and start the game */
        else {
            /* create client */
            String playerName = screenManager.getPlayerName();
            String serverIp = screenManager.getServerIP();
            client = new Client("localhost",playerName,false); //todo utolso argra nincs szukseg
            //TODO localhost hardcodeot kiszedni
            server = null;
            /* wait for server to send init package,
             * containing other the other players' data
             */
            client.receiveFromServerInit();

            /* update numOfPlayers in gameScreen */
            int numOfPlayers = client.board.getScores().length;
            GameScreen gameScr = new GameScreen(numOfPlayers);
            screenManager.setGameScreen(gameScr);

            window.setTitle("Kurve Fívör gui (kliens: #" + client.player.id + ", player: " + client.player.getName() + ")");

            screenManager.update();

            //TODO itt meg kene oldani hogy a player egyhelyben forogjon

            /* enter game loop */
            while (screenManager.getProgramState() == ProgramState.IN_GAME) {

                /* wait for server to request data, and then send it */
                client.sendToServer();

                /* receive game data from the latest cycle */
                PackageS2C message = client.receiveFromServer();
                if (message != null )  {
                    this.client.board.receiveFromPackageS2C(message);
                }

                Board boardToDisplay = this.client.getBoard();
                screenManager.getGameScreen().setCurves(boardToDisplay.getCurves());  //todo ezt is optimalizalni
                screenManager.getGameScreen().setCurrentRound(boardToDisplay.getCurrentRound());
                screenManager.getGameScreen().setPlayerNames(boardToDisplay.getPlayerNames());
                //screenManager.getGameScreen().setNumOfPlayers(boardToDisplay.getRoundNum());
                screenManager.getGameScreen().setScores(boardToDisplay.getScores());
                screenManager.getGameScreen().setRoundNum(boardToDisplay.getRoundNum());


                screenManager.update();
            }
        }
    }
}
