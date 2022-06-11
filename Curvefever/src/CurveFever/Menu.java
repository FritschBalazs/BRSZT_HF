package CurveFever;

import CurveFever.gui.EndGameScreen;
import CurveFever.gui.GameScreen;
import CurveFever.gui.ScreenManager;

import javax.swing.*;

public class Menu {
    private Client client;
    private Server server; /* only one Server or one Client will be used */
    private int prevRound;
    private GameState prevGameState;
    private ScreenManager sManager;

    public Menu(){

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
        window.setLocationRelativeTo(null);  /* locating window in the middle of the screen */
        window.setIconImage(new ImageIcon("src/curvefeverlogo.jpg").getImage()); /* adding logo to window */
        window.setVisible(true);

        EndGameScreen endGameScreen = new EndGameScreen(); /* add gamescreen to ScreenManager */
        sManager.setEndGameScreen(endGameScreen);

        /* main loop. Only the exit button will exit it */
        while(true){

            /* wait for the player to click create game or joing game */
            while (sManager.getProgramState() == ProgramState.MAIN_MENU) {
                sManager.update(true);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            /* if this instance is a server, create the server, and start the game */
            if (sManager.isServer()){

                /* create server */
                int numOfPlayers = sManager.getNumOfPlayers();
                int numOfRounds = sManager.getNumOfRounds();
                String serverPlayerName = sManager.getPlayerName();
                this.server = new Server(numOfPlayers,numOfRounds,serverPlayerName);

                /* update window title */
                window.setTitle("Kurve Fívör(server, player: " + serverPlayerName + ")");

                /* create game screen and add it to sManager */
                GameScreen gameScr = new GameScreen(numOfPlayers);
                sManager.setGameScreen(gameScr);

                /* setup connections and configure game */
                this.server.acceptConnections();
                this.server.setupGame();

                /* get the board object we have to display, and give it to the screen manager */
                Board boardToDisplay = this.server.getGame().getMainBoard();
                initGuiData(boardToDisplay,sManager.getGameScreen());
                sManager.update(true);

                /* set prevRound (end of round detection is based on it) */
                prevRound = boardToDisplay.getCurrentRound();

                /* eneter game loop */
                while (sManager.getProgramState() == ProgramState.IN_GAME) {

                    /* get Control input for local player */
                    server.getPlayer().setControlState(sManager.getControlState());


                    /*                -------------***************---------------
                      server main function is Server.runServer(), which is called by Server.setupGame()
                                   Timer is set up, adn started in Server.setupGame
                                      -------------***************---------------                 */


                    /* wait for timer thread to finish */
                    while(server.waitForDraw()) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }



                    /* update data in GUI classes */
                    if (server.getGame().getGameState() == GameState.PREP) {
                        initGuiData(boardToDisplay,gameScr);  /* in prepmode more data is requried */
                    }
                    else{
                        updateGuiData(boardToDisplay,gameScr);
                    }

                    /* clear gui and board every cycle in prep */
                    if(server.getGame().getGameState() == GameState.PREP || prevGameState == GameState.PREP){
                        /* in prep mode we have to clear the image every time (new rounds also start with prep) */
                        sManager.getGameScreen().getGamePanel().resetBufferedImage();
                    }

                    /* if the game is over display the endscreen */
                    if (server.getGame().getGameState() == GameState.MENU){

                        initEndGameScreenData(boardToDisplay,endGameScreen);
                        sManager.setProgramState(ProgramState.END_OF_GAME);

                        server.sendEndOfGameToClients();
                    }

                    /* call the update method for the GUI */
                    if (server.getGame().getGameState() == GameState.PREP|| prevGameState == GameState.PREP){
                        sManager.update(true);
                    }
                    else
                    {
                        sManager.update(false);
                    }
                    /* set flag for the timer thread */
                    server.drawFinished();
                    prevGameState = server.getGame().getGameState();
                }

                /* enter loop for endGame screen */
                while(sManager.getProgramState() == ProgramState.END_OF_GAME){

                    /* call GUI update methd */
                    sManager.update((false));

                    /* wait for input */
                    if(sManager.getProgramState() == ProgramState.MAIN_MENU){
                        /* return to the main menu */
                        server.closeAllConnections();
                        sManager.deleteGameScreen();
                        sManager.getEndGameScreen().getWaitingTextLabel().setVisible(false);
                        sManager.getMenuScreen().getWaitingTextLabel().setVisible(false);
                        window.setTitle("Kurve Fívör");
                        break;
                    }
                    if(sManager.getProgramState() == ProgramState.IN_GAME){
                        /* replay game with the exact same settings */
                        server.waitForReplayMsgs();
                        System.out.println("Replaying match");
                        server.closeAllConnections();
                    }
                } /* end of endGameScrren while */

            } /* end of server if*/


            /* if this instance is a client, set up the client, and start the game */
            else {
                /* create client */
                String playerName = sManager.getPlayerName();
                String serverIp = sManager.getServerIP();
                client = new Client(serverIp,playerName);

                /* wait for server to send init package,
                 * containing the other players' data
                 */
                client.receiveFromServerInit();


                /* create a gameScrren, then get the board object we want to display */
                int numOfPlayers = client.board.getScores().length;
                GameScreen gameScr = new GameScreen(numOfPlayers);
                sManager.setGameScreen(gameScr);
                window.setTitle("Kurve Fívör (client: #" + client.player.id + ", player: " + client.player.getName() + ")");

                Board boardToDisplay = this.client.getBoard();
                initGuiData(boardToDisplay,sManager.getGameScreen());

                /* set prevRound (end of round detection is based on it)*/
                prevRound = boardToDisplay.getCurrentRound();

                /* call GUI update method */
                sManager.update(true);

                /* enter game loop */
                while (sManager.getProgramState() == ProgramState.IN_GAME) {

                    /* update control state. Note: if this is not working nicely,
                     * it should be moved inside sendToServer somehow */
                    client.getPlayer().setControlState(sManager.getControlState());

                    PackageS2C message; /* container for the message wa will get form the server */

                    /* wait for server to request data. Return value indicates if server requested control input,
                    * or if it signaled that the game is over (this happens after the last round) */
                    if (client.sendToServer() == true)
                    {

                        /* receive game information containing data from the latest cycle */
                        message = client.receiveFromServer();
                        if (message != null )  {
                            if(message.gameState == GameState.PREP || prevGameState == GameState.PREP) {

                                /* in prep mode we have to clear the image every time (new rounds also start with prep) */
                                sManager.getGameScreen().getGamePanel().resetBufferedImage();
                            }

                            if (message.gameState == GameState.PREP){
                                if (prevRound != message.currentRound){
                                    /* at new round we have to clear the curves on the board */
                                    boardToDisplay.clearBoard();
                                }
                                prevRound = message.currentRound;
                            }

                            /* give data to the local board. Only new curve points are sent from the server,
                             *  the local board assembles them into a complete curve
                             */
                            this.client.board.receiveFromPackageS2C(message);

                            /* actualize the data stored in gui classes */
                            if (message.gameState == GameState.PREP || prevGameState == GameState.PREP){
                                initGuiData(boardToDisplay,gameScr); /* in prep mode we need more parameters */
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
                        /* if the game is over, display the endscreen */
                        initEndGameScreenData(boardToDisplay,endGameScreen);
                        sManager.setProgramState(ProgramState.END_OF_GAME);
                    }
                } /* end of game loop */

                /* endGameScreen loop */
                while(sManager.getProgramState() == ProgramState.END_OF_GAME){

                    /* update GUI */
                    sManager.update((false));

                    /* wait for user input */
                    if(sManager.getProgramState() == ProgramState.MAIN_MENU) {
                        /* return main menu */
                        client.closeSocket();
                        sManager.getEndGameScreen().getWaitingTextLabel().setVisible(false);
                        sManager.getMenuScreen().getWaitingTextLabel().setVisible(false);
                        window.setTitle("Kurve Fívör");
                        break;
                    }

                    if (sManager.getProgramState() == ProgramState.IN_GAME) {
                        /* replay game with the same setiings */
                        client.sendReplayRequest();
                        client.waitForReplayMsg();
                        System.out.println("Rematch accepted");
                        client.closeSocket();
                    }
                }
                //TODO (low prio) jar generalas
            } /* end of client if  */
        } /* end of while(1) */
    } /* end of function */
}
