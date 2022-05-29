package CurveFever;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

public class Game {
    private enum circlePart {
        UPPER_RIGHT,
        LOWER_RIGHT,
        LOWER_LEFT,
        UPPER_LEFT
    }

    private enum playerPositions {
        BOTTOM_LEFT,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT
    }

    private static final java.awt.Color[] PlayerColors = {
            Color.RED, Color.ORANGE, Color.PINK, Color.GREEN, Color.YELLOW, Color.BLUE, Color.CYAN};
    private static final double R = 200.0;
    public static final int MAX_PLAYERS = 4;
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_ROUNDS = 10;
    public static final int MIN_ROUNDS = 1;
    public static final int SCORE_PER_SECOND = 50;
    public static final int SYSTEM_TICK = ServerSidePlayer.SYSTEM_TICK;
    public static final double SCORE_PER_TICK = (double) SCORE_PER_SECOND / (double) SYSTEM_TICK;

    private int playerNum;
    private ServerSidePlayer[] Players;
    private int roundNum;
    private int currentRound;
    private Board mainBoard;
    private GameState gameState;

    /*
    ------------------------------------------------------------
    --------- Constructors, getters, setters -------------------
    ------------------------------------------------------------
    */
    public Game(ServerSidePlayer[] Players) {
        this.roundNum = 3;
        this.playerNum = 3;
        this.currentRound = 1;
        Color[] Colors = new Color[playerNum];

        Arrays.fill(Colors, Color.BLACK);

        String[] playerNames = new String[Players.length];
        for (int i = 0; i < Players.length; i++) {
            playerNames[i] = Players[i].getName();
        }
        this.mainBoard = new Board(this.playerNum, this.roundNum, playerNames, Colors);

        this.Players = Players.clone();
        this.gameState = GameState.MENU;

    }

    public Game(int playerNum, int roundNum, ServerSidePlayer[] Players, Color[] Colors) {
        if (roundNum >= MIN_ROUNDS && roundNum < MAX_ROUNDS)
            this.roundNum = roundNum;
        else
            this.roundNum = 3;

        if (Players.length <= MAX_PLAYERS && Players.length >= MIN_PLAYERS)
            this.playerNum = Players.length;
        else
            this.playerNum = 2;

        this.currentRound = 1;
        this.Players = Players.clone();
        //this.mainBoard = new Board(100, 100, playerNum);
        this.gameState = GameState.MENU;

        String[] playerNames = new String[this.playerNum];
        for (int i = 0; i < this.playerNum; i++) {
            playerNames[i] = Players[i].getName();
        }
        this.mainBoard = new Board(this.playerNum, this.roundNum, playerNames, Colors);
    }

    public int getRoundNum() {
        return this.roundNum;
    }

    public int getPlayerNum() {
        return this.playerNum;
    }

    public int getCurrentRound() {
        return this.currentRound;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public Board getMainBoard() {
        return this.mainBoard;
    }

    public double[] getScores() {
        double[] temp = new double[playerNum];
        for (int i = 0; i < Players.length; i++) {
            temp[i] = Players[i].getScore();
        }
        return temp.clone();
    }

    public Color[] getColors() {
        Color[] tempColors = new Color[playerNum];
        for (int i = 0; i < playerNum; i++) {
            tempColors[i] = Players[i].getPlayerColor();
        }
        return tempColors.clone();
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void incrCurrRound() {
        this.currentRound = this.currentRound + 1;
        this.mainBoard.setCurrentRound(mainBoard.getCurrentRound()+1);
    }

    public void setAllPlayersAlive(){
        for (int i = 0; i < playerNum; i++) {
            Players[i].setAlive(true);
        }
    }



    /*
    ------------------------------------------------------------
    ------------ Math for game logic and init ------------------
    ------------------------------------------------------------
    */

    private double adjustDegree(double deg, circlePart cp) {
        double angle = 0;   // Temporary
        switch (cp) {
            case UPPER_RIGHT -> {
                if (deg >= 90 && deg <= 180)
                    angle = deg - 90;
                else if (deg >= 180 && deg <= 270)
                    angle = deg - 180;
                else if (deg >= 270 && deg <= 360)
                    angle = deg - 270;
                else
                    angle = deg;

                return angle;
            }
            case LOWER_RIGHT -> {
                if (deg >= 0 && deg <= 90)
                    angle = deg + 270;
                else if (deg >= 90 && deg <= 180)
                    angle = deg + 180;
                else if (deg >= 180 && deg <= 270)
                    angle = deg + 90;
                else
                    angle = deg;

                return angle;
            }
            case LOWER_LEFT -> {
                if (deg >= 0 && deg <= 90)
                    angle = deg + 180;
                else if (deg >= 90 && deg <= 180)
                    angle = deg + 90;
                else if (deg >= 270 && deg <= 360)
                    angle = deg - 90;
                else
                    angle = deg;

                return angle;
            }
            case UPPER_LEFT -> {
                if (deg >= 0 && deg <= 90)
                    angle = deg + 90;
                else if (deg >= 180 && deg <= 270)
                    angle = deg - 90;
                else if (deg >= 270 && deg <= 360)
                    angle = deg - 180;
                else
                    angle = deg;

                return angle;
            }
            default -> {
                return angle;
            }
        }
    }

    public Vector2D generateRandomPosition(playerPositions playerPosition) {
        double theta, theta_temp;
        double r;
        double xTemp, yTemp;
        double x = 0, y = 0;

        r = R * Math.sqrt(random()) + 50;    // https://stackoverflow.com/questions/5837572/generate-a-random-point-within-a-circle-uniformly
        theta = random() * 2 * Math.PI;
        theta_temp = toDegrees(theta);
        System.out.println("theta_temp: " + theta_temp + ", player position: " + playerPosition);
        switch (playerPosition) {
            case BOTTOM_LEFT -> theta = adjustDegree(theta_temp, circlePart.UPPER_RIGHT);
            case TOP_LEFT -> theta = adjustDegree(theta_temp, circlePart.LOWER_RIGHT);
            case TOP_RIGHT -> theta = adjustDegree(theta_temp, circlePart.LOWER_LEFT);
            case BOTTOM_RIGHT -> theta = adjustDegree(theta_temp, circlePart.UPPER_LEFT);
        }
        xTemp = r * cos(toRadians(theta));
        yTemp = r * sin(toRadians(theta));
        System.out.println("xtemp: " + xTemp + ", ytemp: " + yTemp);
        switch (playerPosition) {
            case BOTTOM_LEFT -> {
                x = abs(xTemp);
                y = mainBoard.getGameHeightHeight() - abs(yTemp);
                break;
            }
            case TOP_LEFT -> {
                x = abs(xTemp);
                y = abs(yTemp);
                break;
            }
            case TOP_RIGHT -> {
                x = mainBoard.getGameWidthWidth() - abs(xTemp);
                y = abs(yTemp);
                break;
            }
            case BOTTOM_RIGHT -> {
                x = mainBoard.getGameWidthWidth() - abs(xTemp);
                y = mainBoard.getGameHeightHeight() - abs(yTemp);
                break;
            }
        }
        Vector2D pos = new Vector2D(x, y);
        System.out.println("theta: " + theta + ", r: " + r);
        System.out.println("x: " + x + " y: " + y);
        System.out.println("--------------------------------------");
        return pos;
    }

    private int countAlivePlayers(){
        int count = 0;
        for (int i = 0; i < playerNum; i++) {
            if (Players[i].getIsAlive())
                count++;
        }
        return count;
    }

    /*
    ------------------------------------------------------------
    -------------- Initialization methods ----------------------
    ------------------------------------------------------------
    */

    private void initPositions() {
        ArrayList<Vector2D> StartingPositions = new ArrayList<>(this.playerNum);
        switch (this.playerNum) {
            case 2: {
                StartingPositions.add(0, generateRandomPosition(playerPositions.TOP_LEFT));
                StartingPositions.add(1, generateRandomPosition(playerPositions.BOTTOM_RIGHT));
            }
            break;
            case 3: {
                StartingPositions.add(0, generateRandomPosition(playerPositions.TOP_LEFT));
                StartingPositions.add(1, generateRandomPosition(playerPositions.TOP_RIGHT));
                StartingPositions.add(2, generateRandomPosition(playerPositions.BOTTOM_LEFT));
            }
            break;
            case 4: {
                StartingPositions.add(0, generateRandomPosition(playerPositions.TOP_LEFT));
                StartingPositions.add(1, generateRandomPosition(playerPositions.TOP_RIGHT));
                StartingPositions.add(2, generateRandomPosition(playerPositions.BOTTOM_LEFT));
                StartingPositions.add(3, generateRandomPosition(playerPositions.BOTTOM_RIGHT));
            }
            break;
        }
        // Randomize starting positions between players
        Collections.shuffle(StartingPositions);
        for (int i = 0; i < Players.length; i++) {
            Players[i].setPosition(StartingPositions.get(i));
            Vector2D speed = new Vector2D(-2, -2);
            Players[i].setSpeed(speed);
        }
    }

    private void initColors() {
        // Create a randomized list from the colors
        ArrayList<Color> Colors = new ArrayList<>(this.playerNum);
        for (int i = 0; i < this.PlayerColors.length; i++) {
            Colors.add(i, PlayerColors[i]);
        }
        Collections.shuffle(Colors);
        // Assign the random color values to the players
        for (int i = 0; i < this.Players.length; i++) {
            this.Players[i].setPlayerColor(Colors.get(i));
        }
    }

    public void initBoard() {
        boolean[] playersAlive = new boolean[playerNum];
        Arrays.fill(playersAlive,true);
        Color[] initColors = new Color[playerNum];
        CurvePoint[] initPoints = new CurvePoint[playerNum];
        Vector2D[] initPositions = new Vector2D[playerNum];
        for (int i = 0; i < playerNum; i++) {
            initColors[i] = Players[i].getPlayerColor();
            initPositions[i] = Players[i].getPosition();
            initPoints[i] = new CurvePoint(initPositions[i].getX(), initPositions[i].getY(), true);
        }
        mainBoard.addCurvePoints(initPoints, playersAlive);
        mainBoard.setCurveColors(initColors);
    }

    public void initGame() {
        initPositions();
        for (int i = 0; i < Players.length; i++) {
            System.out.println("Players[" + i + "] : x: " + Players[i].getPosition().getX() + "; y: " + Players[i].getPosition().getY());
        }
        initColors();
        initBoard();
    }

    // Given three collinear points p, q, r, the function checks if
    // point q lies on line segment 'pr'
    static boolean onSegment(Vector2D p, Vector2D q, Vector2D r) {
        if (q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX()) &&
                q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY()))
            return true;

        return false;
    }

    // To find orientation of ordered triplet (p, q, r).
    // The function returns following values
    // 0 --> p, q and r are collinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise
    static int orientation(Vector2D p, Vector2D q, Vector2D r) {
        // See https://www.geeksforgeeks.org/orientation-3-ordered-points/
        // for details of below formula.
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0) return 0; // collinear

        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    // The main function that returns true if line segment 'p1q1'
    // and 'p2q2' intersect.
    static boolean doIntersect(Vector2D p1, Vector2D q1, Vector2D p2, Vector2D q2) {
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are collinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1 and q2 are collinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2 and p1 are collinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2 and q1 are collinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // Doesn't fall in any of the above cases
    }


    /*
    ------------------------------------------------------------
    -------------- Methods for game running --------------------
    ------------------------------------------------------------
    */

    public void updatePositions(ControlState[] Controls) {
        System.out.println("CHECK length of the curves");
        /*for (int i = 0; i < playerNum; i++) { TODO Debug print, need to be deleted later
            System.out.println("Player ID:" + i + "  Curve length: " + mainBoard.getCurves()[i].getCurveSize());
            System.out.println("******************************");
        }*/
        // Get player states
        boolean[] playersAlive = new boolean[playerNum];
        for (int i = 0; i < playerNum; i++) {
            playersAlive[i] = Players[i].getIsAlive();
        }
        Vector2D pos = new Vector2D(0,0);
        CurvePoint newPosition;/*new CurvePoint[playerNum];*/
        // Check collisions of alive players
        boolean[] collisions = detectCollisions(playersAlive);

        // Set parameters according to collision event
        for (int i = 0; i < Players.length; i++) {
            Players[i].setControlState(Controls[i]);
            if (collisions[i]) {
                Players[i].setAlive(false);
                System.out.println("Collision detected, Player ID: " + i);
            } else if (Players[i].getIsAlive()){
                Players[i].move();
                pos = Players[i].getPosition();
                newPosition = new CurvePoint(pos.getX(), pos.getY(), true);
                mainBoard.addCurvePoint(newPosition, i);
            }
        }
    }

    public boolean[] detectCollisions(boolean[] playersAlive) {
        boolean[] collisionDetected = new boolean[playerNum];
        // Initial value is false
        Arrays.fill(collisionDetected, false);

        Curve[] Curves;
        Curves = mainBoard.getCurves();
        //System.out.println("fasz: "+Curves[0].getCurveSize()); //TEST Dani
        CurvePoint currentPos;
        CurvePoint lastPos;
        // Store the last two points of the players
        /*for (int i = 0; i < playerNum; i++) {
            currentPos[i] = Curves[i].getLastPoint();
            lastPos[i] = Curves[i].getPoint(Curves[i].getCurveSize() - 2);
            // Check if out of board boundaries - TODO Debug célból kommentezve, később fog kelleni - Marci
            /*if ((currentPos[i].getX() > mainBoard.getGameWidthWidth()) || (currentPos[i].getX() < 0)
            || (currentPos[i].getY() > mainBoard.getGameHeightHeight()) || (currentPos[i].getY() < 0))
                collisionDetected[i] = true;*/
        //}
        // Variables to store the point pairs in the Curves
        CurvePoint curveSegment1;
        CurvePoint curveSegment2;
        for (int i = 0; i < playerNum; i++) {   // Iterate over players
            if (playersAlive[i] == true)
                if (Curves[i].getNumOfCurves() > 4) {
                    // Store last two points of the actual player curve
                    currentPos = Curves[i].getLastPoint();
                    lastPos = Curves[i].getPoint(Curves[i].getNumOfCurves() - 2);

                    // Iterate over all of the curves on the board
                    for (int j = 0; j < playerNum; j++) {

                        // Iterate over point pairs of the selected curve
                        for (int k = 0; k < Curves[j].getNumOfCurves() - 2; k++) {
                            // Avoid false detection of the last curve point (actual position)
                            if (!((i == j) && (k >= Curves[j].getNumOfCurves() - 4))) {
                                curveSegment1 = Curves[j].getPoint(k);
                                curveSegment2 = Curves[j].getPoint(k + 1);
                                // Check if points are not in a hole in the curve
                                if (curveSegment1.getIsColored() && curveSegment2.getIsColored())
                                    // Check intersection
                                    if (doIntersect(currentPos, lastPos, curveSegment1, curveSegment2))
                                        collisionDetected[i] = true;
                            }
                        }
                    }
                }
        }
        return collisionDetected;
    }

    public boolean evaluateStep(ControlState[] Controls) {
        boolean endgame;
        double tmpScore[] = new double[playerNum];
        for (int i = 0; i < Players.length; i++) {
            if (Players[i].getIsAlive()) {
                Players[i].updateScore(SCORE_PER_TICK); //TODO (M) Marci azt mondtuk, hogy ez pozicio fuggo is nem? -B
                tmpScore[i] = Players[i].getScore();
            }
        }
        updatePositions(Controls);
        mainBoard.setScores(tmpScore);
        endgame = (countAlivePlayers() == 1);
        return endgame;
    }

    public boolean runGame(ControlState[] Controls, int debugCycleCount) {

        //ez csak teszteleshez kellet, majd alakitsd at ahogy szeretned
        if (gameState == GameState.PLAYING){
            if (evaluateStep(Controls) == true) {
                return true;
            }
        }
        else if (gameState == GameState.PREP) {
            //TODO (M)


            //testcode, amig nincs rendes prepSpeed

            CurvePoint[] arracyCP = new CurvePoint[playerNum];
            Arrays.fill(arracyCP, new CurvePoint(250+debugCycleCount,250+debugCycleCount, true));

            boolean[] arrayTrue = new boolean[playerNum];
            Arrays.fill(arrayTrue,Boolean.TRUE);

            CurvePoint singleCP = new CurvePoint(250 + debugCycleCount, 250 + debugCycleCount, true);


            if(mainBoard.getCurves()[0].getPoints().size() == 1){
                mainBoard.addCurvePoints(arracyCP,arrayTrue);
            }
            else{
                for (int i = 0; i < playerNum; i++) {
                    if(mainBoard.getCurves()[i].getPoints().size() == 2){
                            mainBoard.getCurves()[i].setAPoint(1,singleCP);
                        }
                }
            }

            //end of testcode


        }

        return false;
    }

    //public Curve[] getBoardCurves() {
    //    return mainBoard.getCurves().clone();
    //}

    /* returns the newly calculated CurvePoints */

}


