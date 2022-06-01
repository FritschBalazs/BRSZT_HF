package CurveFever.gui;

import CurveFever.ControlOption;
import CurveFever.ControlState;
import CurveFever.ProgramState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ScreenManager extends JPanel implements ActionListener, KeyListener{
    final static String MENUSCREEN = "menuScreen";
    final static String GAMESCREEN = "gameScreen";
    final static String ENDGAMESCREEN = "endGameScreen";
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private EndGameScreen endGameScreen;
    private ProgramState programState;
    private ProgramState prevProgramState;
    private CardLayout layout = new CardLayout();
    private String playerName;
    private String serverIP;
    private boolean isServer;
    private ControlOption controlOption;
    private int numOfPlayers;
    private int numOfRounds;
    private ControlState controlState;
    private boolean pressedLeft;
    private boolean pressedRight;
    private boolean lastPressedLeft;

    public ScreenManager(){
        setLayout(layout);
        programState = ProgramState.MAIN_MENU; //changed from main menu
        prevProgramState = programState;
        gameScreen = new GameScreen(numOfPlayers);
        menuScreen = new MenuScreen();
        endGameScreen = new EndGameScreen();
        menuScreen.createGameButton.addActionListener(this);
        menuScreen.joinGameButton.addActionListener(this);
        menuScreen.exitButton.addActionListener(this);
        menuScreen.playerNameTextField.addActionListener(this);
        menuScreen.IPTextField.addActionListener(this);
        menuScreen.numOfPlayersComboBox.addActionListener(this);
        endGameScreen.backToMenuButton.addActionListener(this);
        endGameScreen.playAgainButton.addActionListener(this);



        this.add(menuScreen, MENUSCREEN);
        this.add(gameScreen, GAMESCREEN);
        this.add(endGameScreen, ENDGAMESCREEN);
        this.addKeyListener(this);
        this.setFocusable(true);
    }

    public MenuScreen getMenuScreen() {
        return menuScreen;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.add(gameScreen,GAMESCREEN);
    }

    public void deleteGameScreen() { //TODO (B) ezt torolni
        this.gameScreen = null;

    }

    public void setEndGameScreen(EndGameScreen endGameScreen) {
        this.endGameScreen = endGameScreen;
        this.endGameScreen.backToMenuButton.addActionListener(this);
        this.endGameScreen.playAgainButton.addActionListener(this);
        this.add(endGameScreen,ENDGAMESCREEN);
    }

    public GameScreen getGameScreen(){
        return this.gameScreen;
    }

    public ProgramState getProgramState() {
        return programState;
    }

    public void setProgramState(ProgramState programState) {
        this.programState = programState;
    }

    public boolean isServer() {
        return isServer;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public int getNumOfRounds() {
        return numOfRounds;
    }

    public ControlState getControlState() {
        return controlState;
    }

    public EndGameScreen getEndGameScreen() {
        return this.endGameScreen;
    }

    public void update(boolean firstCall){
        switch (programState) {
            case IN_GAME:
                gameScreen.render(firstCall);
                layout.show(this, GAMESCREEN);
                //if(programState != prevProgramState) { //TODO (B/D) ha lesz switch manu akkor ezt lehet optimalizalni
                //    layout.show(this, GAMESCREEN);
                //}
                break;
            case MAIN_MENU:
                layout.show(this, MENUSCREEN);
                //if(programState != prevProgramState) {
                //    layout.show(this, MENUSCREEN);
                //}
                break;
            case END_OF_GAME:
                endGameScreen.render();
                layout.show(this, ENDGAMESCREEN);
                //if(programState != prevProgramState) {
                //    layout.show(this, ENDGAMESCREEN);
                //}
                break;
            default:
                break;
        }
        evaluateInput();
        prevProgramState = programState;
    }

    public void evaluateInput(){
        if(pressedLeft && pressedRight){
            if(lastPressedLeft){
                controlState = ControlState.LEFT;
            }
            else controlState = ControlState.RIGHT;
        } else if (pressedLeft) {
            controlState = ControlState.LEFT;
        } else if (pressedRight) {
            controlState = ControlState.RIGHT;
        }
        else controlState = ControlState.STRAIGHT;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if((e.getSource() == menuScreen.createGameButton)) {
            //TODO (D) check playername, numofrounds
            isServer = true; //ennek elorebb kell lenni mint a program state valtasnak, kulonben nem jo
            playerName = menuScreen.playerNameTextField.getText();
            if(menuScreen.arrowsButton.isSelected()){
                controlOption = ControlOption.ARROW;
            }
            else controlOption = ControlOption.A_D;
            //noinspection ConstantConditions
            numOfPlayers = ((Integer) menuScreen.numOfPlayersComboBox.getSelectedItem());
            numOfRounds = Integer.parseInt(menuScreen.numOfRoundsTextField.getText());
            menuScreen.waitingTextLabel.setVisible(true);
            programState = ProgramState.IN_GAME;

        } else if (e.getSource() == menuScreen.joinGameButton) {
            //TODO (D) check playername,ip
            isServer = false;
            playerName = menuScreen.playerNameTextField.getText();
            serverIP = menuScreen.IPTextField.getText();
            if(menuScreen.arrowsButton.isSelected()){
                controlOption = ControlOption.ARROW;
            }
            else controlOption = ControlOption.A_D;
            menuScreen.waitingTextLabel.setVisible(true);
            programState = ProgramState.IN_GAME;


        } else if (e.getSource() == menuScreen.exitButton) {
            System. exit(0);
        } else if (e.getSource() == endGameScreen.backToMenuButton) {
            programState = ProgramState.MAIN_MENU;
        } else if (e.getSource() == endGameScreen.playAgainButton) {
            endGameScreen.waitingTextLabel.setVisible(true);
            programState = ProgramState.IN_GAME;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if(controlOption == ControlOption.ARROW){
            if(e.getKeyCode() == KeyEvent.VK_LEFT){
                pressedLeft = true;
                lastPressedLeft = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                pressedRight = true;
                lastPressedLeft = false;
            }
        }
        if(controlOption == ControlOption.A_D){
            if(e.getKeyCode() == KeyEvent.VK_A){
                pressedLeft = true;
                lastPressedLeft = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_D){
                pressedRight = true;
                lastPressedLeft = false;
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if(controlOption == ControlOption.ARROW){
            if(e.getKeyCode() == KeyEvent.VK_LEFT){
                pressedLeft = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                pressedRight = false;
            }
        }
        if(controlOption == ControlOption.A_D){
            if(e.getKeyCode() == KeyEvent.VK_A){
                pressedLeft = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_D){
                pressedRight = false;
            }
        }
    }


}

