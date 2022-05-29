package CurveFever;

import CurveFever.gui.EndGameScreen;
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
    private int prevRound;
    private GameState prevGameState;
    //TODO (B) ha nagyjabol kesz a menu osztaly akkor ezt purgalni (nem tudom kell-e majd ujrakezdesnel esetleg pl a numOfRounds)

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

    public void updateGuiData(Board boardToDisplay, GameScreen gameScreen) {
        gameScreen.setCurvePoints(boardToDisplay.getLastCurvePoints());
        gameScreen.setCurrentRound(boardToDisplay.getCurrentRound());
        gameScreen.setScores(boardToDisplay.getScores());
    }

    public void initGuiData(Board boardToDisplay, GameScreen gameScreen){
        gameScreen.setCurvePoints(boardToDisplay.getLastCurvePoints());
        gameScreen.setPrevCurvePoints(boardToDisplay.getLastLastCurvePoints());
        gameScreen.setCurrentRound(boardToDisplay.getCurrentRound());
        gameScreen.setScores(boardToDisplay.getScores());
        gameScreen.setRoundNum(boardToDisplay.getRoundNum());
        gameScreen.setPlayerNames(boardToDisplay.getPlayerNames());
        gameScreen.setNumOfPlayers(boardToDisplay.getNumOfPlayers());
        gameScreen.setColors(boardToDisplay.getColors());
    }
    public void initEndGameScrenData(Board boardToDisplay, EndGameScreen endGameScreen){
        endGameScreen.setScores(boardToDisplay.getScores());
        endGameScreen.setPlayerNames(boardToDisplay.getPlayerNames());
        endGameScreen.setNumOfPlayers(boardToDisplay.getNumOfPlayers());
        endGameScreen.setColors(boardToDisplay.getColors());
    }

    public void runMenu(){
        JFrame window = new JFrame("Kurve Fívör");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ScreenManager screenManager = new ScreenManager();

        window.add(screenManager);
        window.pack();
        window.setLocationRelativeTo(null); //locating window in the middle of the screen
        window.setIconImage(new ImageIcon("src/curvefeverlogo.jpg").getImage()); //adding logo to window
        window.setVisible(true);


        /* wait for the player to start click create game or joing game */
        while (screenManager.getProgramState() == ProgramState.MAIN_MENU) {
            screenManager.update(true);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
/*
        //TESTCODE
        Board Testboard = new Board(4,5,new String[]{"Hanti","Dani", "Frici","valaki"},new Color[]{Color.PINK,Color.PINK,Color.PINK,Color.PINK});
        while (screenManager.getProgramState() == ProgramState.END_OF_GAME) {
            initEndGameScrenData(Testboard,screenManager.getEndGameScreen());
            screenManager.update(true);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        while (screenManager.getProgramState() == ProgramState.MAIN_MENU) {
            screenManager.update(true);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        //ENDOFTESTCODE
*/
        /* if this instance is a server, create the server, and start the game */
        if (screenManager.isServer() == true){

            /* create server */
            int numOfPlayers = screenManager.getNumOfPlayers();
            int numOfRounds = screenManager.getNumOfRounds();
            String serverPlayerName = screenManager.getPlayerName();
            this.server = new Server(numOfPlayers,numOfRounds,serverPlayerName);

            /* update window title */
            window.setTitle("Kurve Fívör(server, player: " + serverPlayerName + ")");

            /* create game screen and add it to screenManager */ //servernel lehetne a scrrenmanager-en belulre, kliensnel nehezebb
            GameScreen gameScr = new GameScreen(numOfPlayers);
            screenManager.setGameScreen(gameScr);

            /* setup connections and configure game */
            this.server.acceptConnections();
            this.server.setupGame();

            Board boardToDisplay = this.server.getGame().getMainBoard();
            initGuiData(boardToDisplay,screenManager.getGameScreen());
            screenManager.update(true);

            /* set prevRound (end of round detection is based on it)*/
            prevRound = boardToDisplay.getCurrentRound();

            /* eneter game loop */
            while (screenManager.getProgramState() == ProgramState.IN_GAME) {

                /* get Control input for local player */
                server.getPlayer().setControlState(screenManager.getControlState());

                /* server main function is Server.runServer(), which is called by a timer */


                /* wait for timer thread to finish */
                while(server.waitForDraw() == true) {}


                /* update data in GUI classes */
                updateGuiData(boardToDisplay,screenManager.getGameScreen());
                if (server.getGame().getGameState() == GameState.PREP) {
                    initGuiData(boardToDisplay,gameScr);
                }
                /* clear gui and board if needed */
                if(server.getGame().getGameState() == GameState.PREP || prevGameState == GameState.PREP){
                    /* in prep mode we have to clear the image every time (new round also starts with prep) */
                    screenManager.getGameScreen().getGamePanel().resetBufferedImage();
                }

                if (server.getGame().getGameState() == GameState.PREP|| prevGameState == GameState.PREP){
                    screenManager.update(true);
                }
                else
                {
                    screenManager.update(false);
                }

                server.drawFinished();
                prevGameState = server.getGame().getGameState();

            }
        }

        /* if this insatance is a client, setup the client, and start the game */
        else {
            /* create client */
            String playerName = screenManager.getPlayerName();
            String serverIp = screenManager.getServerIP();
            client = new Client(serverIp,playerName);

            /* wait for server to send init package,
             * containing other the other players' data
             */
            client.receiveFromServerInit();

            /* update numOfPlayers in gameScreen */
            int numOfPlayers = client.board.getScores().length;
            GameScreen gameScr = new GameScreen(numOfPlayers);
            screenManager.setGameScreen(gameScr);

            window.setTitle("Kurve Fívör (client: #" + client.player.id + ", player: " + client.player.getName() + ")");

            Board boardToDisplay = this.client.getBoard();
            initGuiData(boardToDisplay,screenManager.getGameScreen());

            /* set prevRound (end of round detection is based on it)*/
            prevRound = boardToDisplay.getCurrentRound();

            screenManager.update(true);

            //TODO (B/M) itt meg kene oldani hogy a player egyhelyben forogjon

            /* enter game loop */
            while (screenManager.getProgramState() == ProgramState.IN_GAME) {

                /* update control state. Note: if this is not working nicely,
                 * it should be moved inside sendToServer somehow */
                client.getPlayer().setControlState(screenManager.getControlState());

                /* wait for server to request data, and then send it */
                client.sendToServer();

                /* receive game data containing data from the latest cycle */
                PackageS2C message = client.receiveFromServer();
                if (message != null )  {
                    if(message.gameState == GameState.PREP || prevGameState == GameState.PREP) {

                        /* in prep mode we have to clear the image every time (new round also starts with prep) */
                        screenManager.getGameScreen().getGamePanel().resetBufferedImage();
                    }

                    if (message.gameState == GameState.PREP){
                        if (prevRound != message.currentRound){
                            /* at new round we have to clear the lines on the board */
                            boardToDisplay.clearBoard();
                        }
                        prevRound = message.currentRound;
                    }
                    else if (message.gameState == GameState.PLAYING) {
                        System.out.println("Started playing");
                    }

                    this.client.board.receiveFromPackageS2C(message);
                }

                /* actualize data stored in gui classes */
                if (message.gameState == GameState.PREP || prevGameState == GameState.PREP){
                    initGuiData(boardToDisplay,gameScr);
                    /* draw */
                    screenManager.update(true);
                }
                else {
                    updateGuiData(boardToDisplay,gameScr);
                    /* draw */
                    screenManager.update(false);
                }

                prevGameState = message.gameState;




            }
            //TODO (M/B) kitalalni, hogy mi legyen ha vege egy jatekanak (osszes kornek)
            //TODO (M low prio) lyukak
            //TODO (low prio) jar generalas
        }
    }
}
