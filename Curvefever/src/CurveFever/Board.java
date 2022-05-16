package CurveFever;

import CurveFever.gui.InfoPanel;

import java.awt.*;
import java.awt.Color;
import java.awt.geom.Line2D;
import javax.swing.*;

public class Board extends JPanel {

    private static final int windowWidth = 1280;
    private static final int windowHeight = 720;
    private static final int infoPanelWidth = 250;
    private static final int infoPanelHeight = 720;

    private static final int gameWidth = windowWidth - infoPanelWidth; //actual game board width
    private static final int gameHeight = windowHeight - infoPanelHeight; //actual game board height




    private static final Color WHITE = new Color(255,255,255);
    private static final Color BLACK = new Color(0,0,0);
    private Curve[] Curves;
    private double[] Points;
    private int currentRound;
    private int roundNum;
    private String[] PlayerNames;
    private InfoPanel infoPanel;
    //private InfoPanel infoPanel = new InfoPanel();


    public Board(int numOfPlayers, int numOfRounds, String[] playerNames, Color[] colors) {
        this.Curves = new Curve[numOfPlayers];
        this.Points = new double[numOfPlayers];
        this.currentRound = 0;
        this.roundNum = numOfRounds;
        this.PlayerNames = playerNames.clone();
        this.Points = new double[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            Curves[i] = new Curve();
            Curves[i].setColor(colors[i]);
            Points[i] = 0;
        }
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setBorder(BorderFactory.createLineBorder(Color.pink));
        infoPanel = new InfoPanel(infoPanelWidth,infoPanelHeight);
        infoPanel.setPlayerNames(playerNames);
        infoPanel.setRoundNum(numOfRounds);

    }

    public Board(InitPackageS2C pkg) {
        int numOfPlayers = pkg.playerNames.length;
        this.Curves = new Curve[numOfPlayers];
        for(int i = 0; i < numOfPlayers; i = i + 1) {
            Curves[i] = new Curve();
            Curves[i].setColor(pkg.Colors[i]);
        }
        this.Points = pkg.Scores;
        this.currentRound = pkg.currentRound;
        this.roundNum = pkg.numOfRounds;
        this.PlayerNames = pkg.playerNames.clone();

        setPreferredSize(new Dimension(gameWidth, gameHeight));

        for (int i = 0; i < numOfPlayers; i++) {
            Curves[i] = new Curve();
            Curves[i].setColor(pkg.Colors[i]);
        }

    }
    public void addInfoPanel(){
        this.setLayout(new BorderLayout());
        this.add(infoPanel, BorderLayout.EAST);
        //repaint();
    }


    public void setCurves(Curve[] curves) {Curves = curves.clone();}

    public void setPoints(double[] P) {
        Points = P.clone();
    }


    public void setCurrentRound(int numOfRound) {
        currentRound = numOfRound;
    }

    public void setRoundNum(int numOfRounds) {
        this.roundNum = numOfRounds;
    }

    public void setPlayerNames(String[] names) {
        this.PlayerNames = names.clone();
    }

    public int getGameWidthWidth() {
        return gameWidth;
    }

    public int getGameHeightHeight() {
        return gameHeight;
    }

    public Curve[] getCurves() {return Curves.clone();}

    public double[] getPoints() {
        return Points.clone();
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getRoundNum(){
        return roundNum;
    }

    public String[] getPlayerNames() {
        return PlayerNames.clone();
    }

    public void receiveFromPackageS2C(double[] scores, int currentRound, CurvePoint[] positions) {
        this.currentRound = currentRound;
        for (int i= 0; i < Points.length; i = i +1) {
            Points[i] = scores[i];
            Curves[i].addPoint(positions[i]);
        }
    }

    public void receiveFromPackageS2C(PackageS2C pkg) {
        this.currentRound = pkg.currentRound;
        this.Points = pkg.Scores.clone();

        for (int i= 0; i < Points.length; i = i +1) {
            Curves[i].addPoint(pkg.CurvePoints[i]);
        }
    }

    public void drawCurves(Graphics g) { //not used here, instead in gui

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        for (int i = 0; i < Curves.length; i = i + 1) {
            g2d.setColor(Curves[i].getColor());
            for (int j = 1; j < Curves[i].getPoints().size(); j = j+1) {
                if ((Curves[i].getPoints().get(j).getIsColored()) && (Curves[i].getPoints().get(j-1).getIsColored())) {
                    g2d.draw(new Line2D.Double(Curves[i].getPoints().get(j-1).getX(), Curves[i].getPoints().get(j-1).getY(), Curves[i].getPoints().get(j).getX(), Curves[i].getPoints().get(j).getY()));
                }
            }
        }
    }
    /* //commented out to test gui
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // when calling g.drawImage() we can use "this" for the ImageObserver
        // because Component implements the ImageObserver interface, and JPanel
        // extends from Component. So "this" Board instance, as a Component, can
        // react to imageUpdate() events triggered by g.drawImage()

        // draw our graphics.
        setBackground(BLACK);
        drawCurves(g);
        //drawScore(g); //commented out si I can test the infopanel



        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }*/

    public void render(){//not used, the function is in gui
        infoPanel.setPoints(this.Points);
        infoPanel.setCurrentRound(this.currentRound);
        repaint();
    }

}
