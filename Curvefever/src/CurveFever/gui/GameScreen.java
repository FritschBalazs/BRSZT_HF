package CurveFever.gui;

import CurveFever.ControlState;
import CurveFever.Curve;
import CurveFever.CurvePoint;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

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
    private Curve[] Curves; //majd lehet törölni kell, elég lesz a points is
    private  Color[] Colors;
    private CurvePoint[] CurvePoints;
    private  CurvePoint[] PrevCurvePoints;
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
        gamePanel.setBoardWidth(gamePanelWidth);
        gamePanel.setBoardHeight(gamePanelHeight);
        BufferedImage img = new BufferedImage(gamePanelWidth,gamePanelHeight,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics(); //TODO (B/D) ezt lehetne az uj clean fuggvennyel, ugy tisztabb
        graphics.setPaint(new Color(26, 72, 98)); //bacground
        graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
        graphics.setColor(Color.magenta);
        graphics.setStroke(new BasicStroke(3));
        graphics.drawRect(0,0,img.getWidth()-1, img.getHeight()-1);
        gamePanel.setBoardImage(img);



        infoPanel = new InfoPanel();
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
        add(gamePanel, BorderLayout.CENTER); //first argument was gamepanel
        add(infoPanel, BorderLayout.EAST);

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

    public void setCurvePoints(CurvePoint[] curvePoints) {
        CurvePoints = curvePoints.clone();
    }

    public void setPrevCurvePoints(CurvePoint[] prevCurvePoints) {
        PrevCurvePoints = prevCurvePoints.clone();
    }

    public void setColors(Color[] colors) {
        Colors = colors.clone();
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

    public void render(boolean firstCall){
        if (firstCall){

            infoPanel.setPlayerNames(this.PlayerNames);
            infoPanel.setNumOfPlayers(this.numOfPlayers);
            infoPanel.setRoundNum(this.roundNum);
            infoPanel.setColors(this.Colors);
            gamePanel.setColors(this.Colors);
            gamePanel.setNumOfPlayers(this.numOfPlayers);
            gamePanel.setPrevCurvePoints(this.PrevCurvePoints);
            gamePanel.setPrevPrevCurvePoints(this.PrevCurvePoints); //TODO (D) ezt a prev-prev dolgot irtsuk ki mert nagyon zavaro
            gamePanel.setCurvePoints(this.CurvePoints);
            gamePanel.setInitHappened(true);


            gamePanel.Paths[0].moveTo(CurvePoints[0].getX(),CurvePoints[0].getY()); //TODO (D) ezt nem kene kitorolni? csak lassit szerintem
            gamePanel.Paths[1].moveTo(CurvePoints[1].getX(),CurvePoints[1].getY());
        }
        infoPanel.setScores(this.Scores);
        infoPanel.setCurrentRound(this.currentRound);
        gamePanel.setCurvePoints(this.CurvePoints);

        repaint();
    }
}
