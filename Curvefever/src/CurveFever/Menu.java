package CurveFever;

import CurveFever.gui.EndGameScreen;
import CurveFever.gui.GameScreen;
import CurveFever.gui.ScreenManager;

import javax.swing.*;

public class Menu {
    private Client client;
    private Server server; //only one Server or one Client will be used
    //private ProgramState programState;
    //private ControlOption controlOptions;
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
    //public void setControlOptions(ControlOption controlOptions) {this.controlOptions = controlOptions;}
    public void setServerIp(String serverIp) {this.serverIp = serverIp;}
    public void setPlayerName(String playerName) {this.playerName = playerName;}
    //public void setNumOfPlayers(int numOfPlayers) {this.numOfPlayers = numOfPlayers;}
    //public void setNumOfRounds(int numOfRounds) {this.numOfRounds = numOfRounds;}
    public Client getClient() {return client;}
    public Server getServer() {return server;}
    //public ProgramState getProgramStateState() {return programState;}
    //public ControlOption getControlOptions() {return controlOptions;}
    public String getServerIp() {return serverIp;}
    public String getPlayerName() {return playerName;}
    //public int getNumOfPlayers() {return numOfPlayers;}
    //public int getNumOfRounds() {return numOfRounds;}

    //private GameScreen gameScr;

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
    public void initEndGameScreenData(Board boardToDisplay, EndGameScreen endGameScreen){
        endGameScreen.setScores(boardToDisplay.getScores());
        endGameScreen.setPlayerNames(boardToDisplay.getPlayerNames());
        endGameScreen.setNumOfPlayers(boardToDisplay.getNumOfPlayers());
        endGameScreen.setColors(boardToDisplay.getColors());
    }

    public void runMenu(){
        JFrame window = new JFrame("Kurve Fívör");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sManager = new ScreenManager();

        window.add(sManager);
        window.pack();
        window.setLocationRelativeTo(null); //locating window in the middle of the screen
        window.setIconImage(new ImageIcon("src/curvefeverlogo.jpg").getImage()); //adding logo to window
        window.setVisible(true);

        EndGameScreen endGameScreen = new EndGameScreen();
        sManager.setEndGameScreen(endGameScreen);


        while(true){

            /* wait for the player to start click create game or joing game */
            while (sManager.getProgramState() == ProgramState.MAIN_MENU) {
                sManager.update(true);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            /* if this instance is a server, create the server, and start the game */
            if (sManager.isServer() == true){

                /* create server */
                int numOfPlayers = sManager.getNumOfPlayers();
                int numOfRounds = sManager.getNumOfRounds();
                String serverPlayerName = sManager.getPlayerName();
                this.server = new Server(numOfPlayers,numOfRounds,serverPlayerName);

                /* update window title */
                window.setTitle("Kurve Fívör(server, player: " + serverPlayerName + ")");

                /* create game screen and add it to sManager */ //servernel lehetne a scrrenmanager-en belulre, kliensnel nehezebb
                GameScreen gameScr = new GameScreen(numOfPlayers);
                sManager.setGameScreen(gameScr);

                /* setup connections and configure game */
                this.server.acceptConnections();
                this.server.setupGame();

                Board boardToDisplay = this.server.getGame().getMainBoard();
                initGuiData(boardToDisplay,sManager.getGameScreen());
                sManager.update(true);

                /* set prevRound (end of round detection is based on it)*/
                prevRound = boardToDisplay.getCurrentRound();

                /* eneter game loop */
                while (sManager.getProgramState() == ProgramState.IN_GAME) {

                    /* get Control input for local player */
                    server.getPlayer().setControlState(sManager.getControlState());

                    /* server main function is Server.runServer(), which is called by a timer */


                    /* wait for timer thread to finish */
                    while(server.waitForDraw()) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }



                    /* update data in GUI classes */
                    updateGuiData(boardToDisplay,gameScr);
                    System.out.println("Update called from menu");
                    if (server.getGame().getGameState() == GameState.PREP) {
                        initGuiData(boardToDisplay,gameScr);
                    }
                    /* clear gui and board if needed */
                    if(server.getGame().getGameState() == GameState.PREP || prevGameState == GameState.PREP){
                        /* in prep mode we have to clear the image every time (new round also starts with prep) */
                        sManager.getGameScreen().getGamePanel().resetBufferedImage();
                    }

                    /* if the game is over display endscreen */
                    if (server.getGame().getGameState() == GameState.MENU){

                        initEndGameScreenData(boardToDisplay,endGameScreen); //TODO (B) ezt majd cleanelni
                        sManager.setProgramState(ProgramState.END_OF_GAME);

                        server.sendEndOfGameToClients();
                    }


                    if (server.getGame().getGameState() == GameState.PREP|| prevGameState == GameState.PREP){
                        sManager.update(true);
                    }
                    else
                    {
                        sManager.update(false);

                    }

                    server.drawFinished();
                    prevGameState = server.getGame().getGameState();

                }

                while(sManager.getProgramState() == ProgramState.END_OF_GAME){//TODO ezt torolni

                    sManager.update((false));
                    if(sManager.getProgramState() == ProgramState.MAIN_MENU){

                        server.closeAllConnections();
                        sManager.deleteGameScreen();
                        break; //TODO (D) ugyanaz mint kliensnel
                    }
                    if(sManager.getProgramState() == ProgramState.IN_GAME){
                        server.waitForReplayMsgs();
                        //endOfEndScreen = true;
                        System.out.println("Replaying match");
                        server.closeAllConnections();
                        sManager.deleteGameScreen();

                    }

                    /*
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }

            }


            /* if this insatance is a client, setup the client, and start the game */
            else {
                /* create client */
                String playerName = sManager.getPlayerName();
                String serverIp = sManager.getServerIP();
                client = new Client(serverIp,playerName);

                /* wait for server to send init package,
                 * containing other the other players' data
                 */
                client.receiveFromServerInit();


                /* update numOfPlayers in gameScreen */
                int numOfPlayers = client.board.getScores().length;
                GameScreen gameScr = new GameScreen(numOfPlayers);
                sManager.setGameScreen(gameScr);

                window.setTitle("Kurve Fívör (client: #" + client.player.id + ", player: " + client.player.getName() + ")");

                Board boardToDisplay = this.client.getBoard();
                initGuiData(boardToDisplay,sManager.getGameScreen());

                /* set prevRound (end of round detection is based on it)*/
                prevRound = boardToDisplay.getCurrentRound();

                sManager.update(true);

                //TODO (B/M) itt meg kene oldani hogy a player egyhelyben forogjon

                /* enter game loop */
                while (sManager.getProgramState() == ProgramState.IN_GAME) {

                    /* update control state. Note: if this is not working nicely,
                     * it should be moved inside sendToServer somehow */
                    client.getPlayer().setControlState(sManager.getControlState());

                    /* wait for server to request data, and then send it */
                    PackageS2C message;
                    if (client.sendToServer() == true)
                    {

                        /* receive game data containing data from the latest cycle */
                        message = client.receiveFromServer();
                        if (message != null )  {
                            if(message.gameState == GameState.PREP || prevGameState == GameState.PREP) {

                                /* in prep mode we have to clear the image every time (new round also starts with prep) */
                                sManager.getGameScreen().getGamePanel().resetBufferedImage();
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

                            /* actualize data stored in gui classes */
                            if (message.gameState == GameState.PREP || prevGameState == GameState.PREP){
                                initGuiData(boardToDisplay,gameScr);
                                /* draw */
                                sManager.update(true);
                            }
                            else {
                                updateGuiData(boardToDisplay,gameScr);
                                /* draw */
                                sManager.update(false);
                            }

                            prevGameState = message.gameState;
                        }
                    }
                    else{
                        initEndGameScreenData(boardToDisplay,endGameScreen); //TODO (B) ezt majd cleanelni
                        sManager.setProgramState(ProgramState.END_OF_GAME);
                    }
                }

                while(sManager.getProgramState() == ProgramState.END_OF_GAME){//TODO ezt torolni

                    sManager.update((false));
                    if(sManager.getProgramState() == ProgramState.MAIN_MENU) {
                        client.closeSocket();
                        break; //TODO (D) waiting for other players felirat látszik amikor visszalep a menube
                    }

                    if (sManager.getProgramState() == ProgramState.IN_GAME) {
                        client.sendReplayRequest();
                        client.waitForReplayMsg();
                        System.out.println("Rematch accepted");
                        client.closeSocket();

                    }

                        /*try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                }
                //TODO (M/B) kitalalni, hogy mi legyen ha vege egy jatekanak (osszes kornek)
                //TODO (M low prio) lyukak
                //TODO (low prio) jar generalas
            }
        }
    }
}
