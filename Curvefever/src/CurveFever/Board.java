package CurveFever;

import java.awt.Color;

public class Board{
    private static final int gameWidth = 1280; //actual game board width
    private static final int gameHeight = 720; //actual game board height
    private Curve[] Curves;
    private double[] Scores;
    private int currentRound;
    private int roundNum;
    private int numOfPlayers;
    private String[] PlayerNames;
    private GameState prevGameState;

    public Board(int numOfPlayers, int numOfRounds, String[] playerNames, Color[] colors) {
        this.numOfPlayers = numOfPlayers;
        this.Curves = new Curve[numOfPlayers];
        this.Scores = new double[numOfPlayers];
        this.currentRound = 1;
        this.roundNum = numOfRounds;
        this.PlayerNames = playerNames.clone();
        this.Scores = new double[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            Curves[i] = new Curve();
            Curves[i].setColor(colors[i]);
            //Curves[i].addPoint(new CurvePoint());  //miert adunk nullat? ne mar
            Scores[i] = 0;
        }
    }

    public Board(InitPackageS2C pkg) {
        numOfPlayers = pkg.playerNames.length;
        this.Curves = new Curve[numOfPlayers];
        for(int i = 0; i < numOfPlayers; i = i + 1) {
            Curves[i] = new Curve();
            Curves[i].addPoint(pkg.CurvePoints[i]);
            Curves[i].setColor(pkg.Colors[i]);
        }
        this.Scores = pkg.Scores;
        this.currentRound = pkg.currentRound;
        this.roundNum = pkg.numOfRounds;
        this.PlayerNames = pkg.playerNames.clone();

    }

    public void setCurves(Curve[] curves) {Curves = curves.clone();}

    public void setScores(double[] P) {
        Scores = P.clone();
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

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public Curve[] getCurves() {return Curves.clone();}

    public double[] getScores() {
        return Scores.clone();
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

    public CurvePoint[] getLastCurvePoints() {
        CurvePoint[] retval = new CurvePoint[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            retval[i] = Curves[i].getLastPoint();
        }
        return retval.clone();
    }

    public CurvePoint[] getLastLastCurvePoints() {
        CurvePoint[] retval = new CurvePoint[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            retval[i] = Curves[i].getLastLastPoint();
        }
        return retval.clone();
    }
    public Color[] getColors(){
        Color[] retval = new Color[numOfPlayers];
        for(int i = 0; i < numOfPlayers; i = i + 1){
            retval[i] = Curves[i].getColor();
        }
        return retval.clone();
    }

    public void setCurveColors(Color[] colors) {
        for (int i = 0; i < numOfPlayers; i++) {
            Curves[i].setColor(colors[i]);
        }
    }

    public void addCurvePoints(CurvePoint[] newPositions, boolean[] playersAlive) {
        for (int i = 0; i < numOfPlayers; i++) {
            if (playersAlive[i])
                Curves[i].addPoint(newPositions[i]);
        }
    }

    public void deleteLastCurvePoints(){
        for (int i = 0; i < numOfPlayers; i++) {
            if (Curves[i].getPoints().size() >= 1)
                Curves[i].deleteLastPoint();
        }
    }

    public void addCurvePoint(CurvePoint newPosition, int idx) {
        Curves[idx].addPoint(newPosition);
    }

    public void receiveFromPackageS2C(double[] scores, int currentRound, CurvePoint[] positions) {
        this.currentRound = currentRound;
        for (int i = 0; i < numOfPlayers; i = i +1) {
            Scores[i] = scores[i];
            Curves[i].addPoint(positions[i]);
        }
    }

    public void receiveFromPackageS2C(PackageS2C pkg) {
        this.currentRound = pkg.currentRound;
        this.Scores = pkg.Scores.clone();


        if (pkg.gameState == GameState.PREP){

            /* in prep state we want to rotate around the starting position */
            for (int i = 0; i < Scores.length; i++) {

                CurvePoint indicatorPoint = new CurvePoint(pkg.prepSpeed[i].x,pkg.prepSpeed[i].y,true);


                /* if the curves are empty, or they have 1 element, add first 2 elements element*/ //note: in the first round they will have 1 element, but when starting the 2nd round they will have nonve
                if (Curves[i].getPoints().size() == 0 || Curves[i].getPoints().size() == 1) {
                    Curves[i].deleteLastPoint();

                    /* add starting pos, and perpSpeed */
                    Curves[i].addPoint(pkg.CurvePoints[i]);
                    Curves[i].addPoint(indicatorPoint);
                }
                /* if not, just replace the last (1st) element */
                else {
                    /* set second point in array list, to the speed value, to rotate in 1 place */
                    Curves[i].setAPoint(1,indicatorPoint);
                }
            }

            prevGameState = GameState.PREP;

        }
        /* in normal mode we just add, the newest points */
        else if (pkg.gameState == GameState.PLAYING) {
            for (int i = 0; i < Scores.length; i = i + 1) {
                /* if this is the first call, delete the prep point */
                if(prevGameState == GameState.PREP){
                    Curves[i].deleteLastPoint();   /* delete */
                    Curves[i].addPoint(pkg.CurvePoints[i]);   /* add new point */
                }
                /* normal add */
                else {
                    Curves[i].addPoint(pkg.CurvePoints[i]);
                }
            }
        }
    }

    public void clearBoard(){
        for (int i = 0; i < numOfPlayers; i++) {
            Curves[i].clearCurves();
        }
    }
}
