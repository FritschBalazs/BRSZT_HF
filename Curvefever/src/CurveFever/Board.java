package CurveFever;

import java.util.ArrayList;

public class Board {
    private static final int width = 720;
    private static final int height = 480;
    private ArrayList<CurvePoint>[] Curves;   //We didn't implement the Curve class, instead we've used this approach.
                                              // array lists: https://www.w3schools.com/java/java_arraylist.asp
    private double[] Points;
    private int currentRound;
    private int roundNum;
    private String[] PlayerNames;

    public Board(int numOfPlayers, int numOfRounds, String[] playerNames ) {
        this.Curves = new ArrayList[numOfPlayers];
        this.Points = new double[numOfPlayers];
        this.currentRound = 0;
        this.roundNum = numOfRounds;
        this.PlayerNames = playerNames.clone();
    }

    public Board(InitPackageS2C pkg) {
        int numOfPlayers = pkg.playerNames.length;
        this.Curves = new ArrayList[numOfPlayers];
        this.Points = pkg.Scores;
        this.currentRound = pkg.currentRound;
        this.roundNum = pkg.numOfRounds;
        this.PlayerNames = pkg.playerNames;
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

    public ArrayList<CurvePoint>[] getCurves() {
        return Curves;
    }

    public double[] getPoints() {
        return Points;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getRoundNum(){
        return roundNum;
    }

    public String[] getPlayerNames() {
        return PlayerNames;
    }

    public void receivePositions (CurvePoint[] positions) {

    }

    public void receiveGameData(double[] scores, int currentRound) {

    }

    public void draw() {

    }


}
