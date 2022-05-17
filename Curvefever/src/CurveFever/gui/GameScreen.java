package CurveFever.gui;

import CurveFever.Curve;
import CurveFever.Game;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingConstants.CENTER;

//Contains GamePanel and InfoPanel
public class GameScreen extends JPanel {
    /*private static final int screenWidth = 1280;
    private static final int screenHeight = 720;*/
    private static final int gamePanelWidth = 1280;
    private static final int gamePanelHeight = 720;
    private static final int infoPanelWidth = 200;
    private static final int infoPanelHeight = gamePanelHeight;

    private GamePanel gamePanel;
    private InfoPanel infoPanel;
    private JLabel footer;
    private Curve[] Curves;
    private double[] Points;
    private int currentRound;
    private int roundNum;
    private String[] PlayerNames;
    private int numOfPlayers;
    //private static LayoutManager layout = new BorderLayout(0,0);

    public GameScreen(int numOfPlayers){
        this.numOfPlayers = numOfPlayers;
        //setBackground(new Color(128, 164, 252)); //same as infoscreen
        setLayout(new BorderLayout(0,0));
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(gamePanelWidth,gamePanelHeight)); //was preferred



        infoPanel = new InfoPanel(numOfPlayers);
        infoPanel.setPreferredSize(new Dimension(infoPanelWidth,infoPanelHeight)); //was preferred

        footer = new JLabel();
        footer.setPreferredSize(new Dimension(gamePanelWidth+infoPanelWidth,15));
        footer.setFont(new Font("Lato", Font.PLAIN, 10));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setHorizontalAlignment(CENTER);
        footer.setForeground(Color.black);
        footer.setText("Kurve Fíver Inc. Copyright, All rights reserved");
        //hateret+atlatszosagot beallitani
        add(footer, BorderLayout.SOUTH);
        add(gamePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
        //infoPanel.setPlayerNames(this.PlayerNames);
    }

    public GameScreen(Game game){
        this.numOfPlayers = game.getPlayerNum();
        this.Curves = game.getBoardCurves().clone();
        this.PlayerNames = game.getMainBoard().getPlayerNames();
        this.Points = game.getScores();
        this.currentRound = game.getCurrentRound();
        this.roundNum = game.getRoundNum();

        setLayout(new BorderLayout(0,0));

        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(gamePanelWidth,gamePanelHeight)); //was preferred

        infoPanel = new InfoPanel(numOfPlayers);
        infoPanel.setPreferredSize(new Dimension(infoPanelWidth,infoPanelHeight)); //was preferred
        footer = new JLabel();
        footer.setPreferredSize(new Dimension(gamePanelWidth+infoPanelWidth,15));
        footer.setFont(new Font("Lato", Font.PLAIN, 10));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setHorizontalAlignment(CENTER);
        footer.setForeground(Color.black);
        footer.setText("Kurve Fíver Inc. Copyright, All rights reserved");
        //hateret+atlatszosagot beallitani
        add(footer, BorderLayout.SOUTH);
        add(gamePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
        //infoPanel.setPlayerNames(this.PlayerNames);
    }

    public void setPlayerNames(String[] playerNames) {
        PlayerNames = playerNames;
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

    public void setPoints(double[] points) {
        Points = points;
    }

    public void setCurves(Curve[] curves) {
        Curves = curves;
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    /*@Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        infoPanel.setMinimumSize(new Dimension(this.getSize().width-gamePanel.getSize().width,gamePanel.getSize().height));

        System.out.println(infoPanel.getSize());
        Toolkit.getDefaultToolkit().sync();
    }*/

    public void render(){
        infoPanel.setPoints(this.Points);
        infoPanel.setCurrentRound(this.currentRound);
        infoPanel.setPlayerNames(this.PlayerNames);
        infoPanel.setNumOfPlayers(numOfPlayers);
        //System.out.println(numOfPlayers);
        Color[] colors = new Color[numOfPlayers];  //this only needed for testing purposes. In creation time Colors will be known from initpackage
        for (int i = 0; i < numOfPlayers; i = i +1) {
            colors[i] = Curves[i].getColor();
        }
        infoPanel.setColors(colors);


        gamePanel.setCurves(this.Curves);
        repaint();
    }

}
