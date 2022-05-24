package CurveFever;

import CurveFever.gui.GameScreen;
import CurveFever.gui.ScreenManager;

import javax.swing.*;
import java.awt.*;

public class Menu {
    private Client client;
    private Server server; //only one Server or one Client will be used
    //private ProgramState programState;
    private ControlOptions controlOptions;
    private String serverIp; //To give to Server or Client constructor
    private String playerName; //To give to Player constructor (player name can be entered in menu, in a textbox)
    //private int numOfPlayers;
    //private int numOfRounds;
    //TODO ha nagyjabol kesz a menu osztaly akkor ezt purgalni

    private ScreenManager sManager;

    public Menu(){}

    //public void setProgramState(ProgramState programState) {this.programState = programState;}
    public void setControlOptions(ControlOptions controlOptions) {this.controlOptions = controlOptions;}
    public void setServerIp(String serverIp) {this.serverIp = serverIp;}
    public void setPlayerName(String playerName) {this.playerName = playerName;}
    //public void setNumOfPlayers(int numOfPlayers) {this.numOfPlayers = numOfPlayers;}
    //public void setNumOfRounds(int numOfRounds) {this.numOfRounds = numOfRounds;}
    public Client getClient() {return client;}
    public Server getServer() {return server;}
    //public ProgramState getProgramStateState() {return programState;}
    public ControlOptions getControlOptions() {return controlOptions;}
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
        ScreenManager screenManager = new ScreenManager(4);

        window.add(screenManager);
        window.pack();
        window.setVisible(true);

        /* wait for the player to start click create game or joing game */
        while (screenManager.getProgramState() != ProgramState.MAIN_MENU) {
            screenManager.update();
        }

        /* if this instance is a server, create the server, and start the game */
        if (screenManager.getIsServer == true){
            /* create server */
            int numOfPlayers = screenManager.getNumOfPlayers();
            int numOfRounds = screenManager.getNumOfRounds();
            String serverPlayerName = screenManager.getPlayerName();
            this.server = new Server(numOfPlayers,numOfRounds,serverPlayerName);
            this.client = null;

            /* setup connections and configure game */
            this.server.acceptConnections();
            this.server.setupGame();

            /* eneter game loop */
            while (screenManager.getProgramState() == ProgramState.IN_GAME) {
                Board boardToDisplay = this.server.getGame().getMainBoard(); //todo ezt optimalizalni
                screenManager.getGameScreen().setCurves(boardToDisplay.getCurves());
                screenManager.getGameScreen().setCurrentRound(boardToDisplay.getCurrentRound());
                screenManager.getGameScreen().setPlayerNames(boardToDisplay.getPlayerNames());
                screenManager.getGameScreen().setNumOfPlayers(boardToDisplay.getRoundNum());
                screenManager.getGameScreen().setPoints(boardToDisplay.getPoints());

                screenManager.update();
            }
        }

        /* if this insatance is a client, setup the client, and start the game */
        else {
            /* create client */
            String playerName = screenManager.getPlayerName();
            String serverIp = screenManager.getServerIp();
            client = new Client(serverIp,playerName,false); //todo utolso argra nincs szukseg
            server = null;
            /* wait for server to send init package,
             * containing other the other players' data
             */
            client.receiveFromServerInit();

            //TODO itt meg kene oldani hogy a player egyhelyben forogjon

            /* enter game loop */
            while (screenManager.getProgramState() == ProgramState.IN_GAME) {

                /* wait for server to request data, and then send it */
                client.sendToServer();

                /* receive game data from the latest cycle */
                PackageS2C message = myClient.receiveFromServer();
                if (message != null )  {
                    this.client.board.receiveFromPackageS2C(message);
                }

                Board boardToDisplay = this.client.getBoard();
                screenManager.getGameScreen().setCurves(boardToDisplay.getCurves());  //todo ezt is optimalizalni
                screenManager.getGameScreen().setCurrentRound(boardToDisplay.getCurrentRound());
                screenManager.getGameScreen().setPlayerNames(boardToDisplay.getPlayerNames());
                screenManager.getGameScreen().setNumOfPlayers(boardToDisplay.getRoundNum());
                screenManager.getGameScreen().setPoints(boardToDisplay.getPoints());

                screenManager.update();
            }
        }
    }
}
