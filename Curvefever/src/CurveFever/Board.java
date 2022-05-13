package CurveFever;

import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.util.ArrayList;

import static java.lang.Boolean.TRUE;

public class Board extends JPanel {
    private static final int width = 1280;
    private static final int height = 720;
    private static final Color WHITE = new Color(255,255,255);
    private static final Color BLACK = new Color(0,0,0);
    private Curve[] Curves;
    private double[] Points;
    private int currentRound;
    private int roundNum;
    private String[] PlayerNames;

    public Board(int numOfPlayers, int numOfRounds) {
        this.Curves = new ArrayList[numOfPlayers];
        this.Points = new int[numOfPlayers];
        this.currentRound = 0;
        this.roundNum = numOfRounds;
        this.PlayerNames = new String[numOfPlayers];
    }

        this.currentRound = 0;
        this.roundNum = numOfRounds;
        this.PlayerNames = playerNames.clone();
        this.Points = new double[numOfPlayers];

        setPreferredSize(new Dimension(width, height));


    }

    public Board(InitPackageS2C pkg) {
        int numOfPlayers = pkg.playerNames.length;
        this.Curves = new Curve[numOfPlayers];
        for(int i = 0; i < numOfPlayers; i = i +1) {
            Curves[i].setColor(pkg.Colors[i]);
        }
        this.Points = pkg.Scores;
        this.currentRound = pkg.currentRound;
        this.roundNum = pkg.numOfRounds;
        this.PlayerNames = pkg.playerNames.clone();

        setPreferredSize(new Dimension(width, height));

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

    public void drawCurves(Graphics g) {

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
    public void drawScore(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(WHITE);
        g2d.drawRect(width-150,2,148,PlayerNames.length*50);
        g2d.setFont(new Font("Lato", Font.BOLD, 20));
        g2d.drawString("Scores:", width-125,  20);
        g2d.setFont(new Font("Lato", Font.PLAIN, 15));
        int y = 20;
        for (int i = 0; i < PlayerNames.length; i = i + 1) {
            g2d.drawString(PlayerNames[i] + ": " + Points, width-110, y += 15);
        }
    }


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
        drawScore(g);
        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }
}
