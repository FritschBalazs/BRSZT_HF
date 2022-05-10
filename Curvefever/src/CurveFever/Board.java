package CurveFever;

import java.util.ArrayList;

public class Board {
    private int width, height;
    private ArrayList<CurvePoint>[] Curves;   //We didn't implement the Curve class, instead we've used this approach.
                                              // array lists: https://www.w3schools.com/java/java_arraylist.asp
    private double[] Points;
    private int currentRound;

    public Board(int w, int h, int numOfPlayers) {
        width = w;
        height = h;
        Curves = new ArrayList[numOfPlayers];
        Points = new double[numOfPlayers];
        currentRound = 0;
    }

    public void setSize(int w, int h) {
        width = w;
        height = h;
    }

    public void setCurves(ArrayList[] C) {
        Curves = C;
    }

    public void setPoints(double[] P) {
        Points = P;
    }

    public void setCurrentRound(int numOfRound) {
        currentRound = numOfRound;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<CurvePoint>[] getCurves() {
        return Curves;
    }

    public double[] getPoints() {
        return Points;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void receivePositions (CurvePoint[] positions) {

    }

    public void receiveGameData(double[] scores, int currentRound) {

    }

    public void draw() {

    }


}
