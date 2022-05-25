package CurveFever.gui;

import CurveFever.ControlState;
import CurveFever.Curve;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingConstants.CENTER;

//Contains GamePanel and InfoPanel
public class GameScreen extends JPanel {
    private static final int gamePanelWidth = 1280;
    private static final int gamePanelHeight = 720;
    private static final int infoPanelWidth = 200;
    private static final int infoPanelHeight = gamePanelHeight;
    private GamePanel gamePanel;
    private InfoPanel infoPanel;
    private JLabel footer;
    private Curve[] Curves;
    private double[] Scores;
    private int currentRound;
    private int roundNum;
    private String[] PlayerNames;
    private int numOfPlayers;
    private ControlState[] controlStates;

    public GameScreen(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
        setLayout(new BorderLayout(0, 0));
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(gamePanelWidth, gamePanelHeight));



        infoPanel = new InfoPanel(numOfPlayers);
        infoPanel.setPreferredSize(new Dimension(infoPanelWidth,infoPanelHeight));

        footer = new JLabel();
        footer.setPreferredSize(new Dimension(gamePanelWidth+infoPanelWidth,15));
        footer.setFont(new Font("Lato", Font.PLAIN, 10));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setHorizontalAlignment(CENTER);
        footer.setForeground(Color.black);
        footer.setBackground(Color.white);
        footer.setText("Kurve Fívör Inc. Copyright, All rights reserved");
        footer.setOpaque(true);

        add(footer, BorderLayout.SOUTH);
        add(gamePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
        //infoPanel.setPlayerNames(this.PlayerNames);
    }

    public void setPlayerNames(String[] playerNames) {
        PlayerNames = playerNames.clone();
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }

    public void setScores(double[] scores) {
        Scores = scores.clone();
    }

    public void setCurves(Curve[] curves) {
        Curves = curves.clone();
    }
    public String[] getPlayerNames(){
        return PlayerNames;
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void render(){
        //TODO ezt torolni mert ez igy eleg troger. Inditasnal hibat dob ha hamarabb lefut az uodate mint a setName()
        if (PlayerNames == null) {
            return;
        }

        infoPanel.setPoints(this.Scores);
        infoPanel.setCurrentRound(this.currentRound);
        infoPanel.setPlayerNames(this.PlayerNames);
        infoPanel.setNumOfPlayers(numOfPlayers);    //TODO cleanup majd egyszer, itt csak azt kene atkuldeni ami valtozik

        Color[] colors = new Color[numOfPlayers];  //this only needed for testing purposes. In creation time Colors will be known from initpackage
        for (int i = 0; i < numOfPlayers; i = i +1) {
            colors[i] = Curves[i].getColor();
        }
        infoPanel.setColors(colors);

        gamePanel.setCurves(this.Curves);
        //infoPanel.setPlayerNames(this.PlayerNames);
        repaint();
    }
}
