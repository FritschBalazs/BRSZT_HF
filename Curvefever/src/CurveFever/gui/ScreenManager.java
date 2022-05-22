package CurveFever.gui;

import CurveFever.ProgramState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScreenManager extends JPanel implements ActionListener{
    final static String MENUSCREEN = "menuScreen";
    final static String GAMESCREEN = "gameScreen";
    final static String ENDGAMESCREEN = "endGameScreen";
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private EndGameScreen endGameScreen;
    private ProgramState programState;
    private ProgramState prevProgramState;
    private CardLayout layout = new CardLayout();
    public ScreenManager(int numOfPlayers){
        setLayout(layout);
        programState = ProgramState.MAIN_MENU; //changed from main menu
        prevProgramState = programState;
        gameScreen = new GameScreen(numOfPlayers);
        menuScreen = new MenuScreen();
        endGameScreen = new EndGameScreen();
        menuScreen.createGameButton.addActionListener(this);
        menuScreen.joinGameButton.addActionListener(this);
        menuScreen.exitButton.addActionListener(this);
        this.add(menuScreen, MENUSCREEN);
        this.add(gameScreen, GAMESCREEN);
        this.add(endGameScreen, ENDGAMESCREEN);
    }

    public MenuScreen getMenuScreen() {
        return menuScreen;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.add(gameScreen,GAMESCREEN);
    }
    public GameScreen getGameScreen(){
        return this.gameScreen;
    }

    public void update(){
        if(programState != prevProgramState){
            switch (programState) {
                case IN_GAME: gameScreen.render();
                    layout.show(this, GAMESCREEN);
                    break;
                case MAIN_MENU: layout.show(this, MENUSCREEN);
                    System.out.println("menucase");
                    break;
                case END_OF_GAME: layout.show(this, ENDGAMESCREEN);
                    break;
                default: System.out.println("Nemmukszik");
                    break;
            }
        }
        prevProgramState = programState;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if((e.getSource() == menuScreen.createGameButton) || (e.getSource() == menuScreen.joinGameButton)) {
            programState = ProgramState.IN_GAME;
        } else if (e.getSource() == menuScreen.exitButton) {
            System.out.println("Valamit meghívni h kilepjen az alkalmazás");
        }
    }
}
