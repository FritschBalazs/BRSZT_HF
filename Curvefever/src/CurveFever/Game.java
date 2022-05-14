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
    private static final double R = 10.0;
    public static final int MAX_PLAYERS = 4;
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_ROUNDS = 10;
    public static final int MIN_ROUNDS = 1;
    public static final int SCORE_PER_SECOND = 50;
    public static final int SYSTEM_TICK = 10;
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
        this.currentRound = 0;

        String[] playerNames = new String[Players.length];
        for (int i = 0; i < Players.length; i++) {
            playerNames[i] = Players[i].getName();
        }
        this.mainBoard = new Board(this.playerNum, this.roundNum, playerNames);

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

        this.currentRound = 0;
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

    /*
    ------------------------------------------------------------
    ------------ Math for game logic and init ------------------
    ------------------------------------------------------------
    */

    private double adjustDegree(double deg, circlePart cp) {
        double angle = 0;   // Temporary
        switch (cp) {
            case UPPER_RIGHT -> {
                if (deg >= toRadians(90) && deg <= toRadians(180))
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
                if (deg >= toRadians(0) && deg <= toRadians(90))
                    angle = deg + 90;
                else if (deg >= 180 && deg <= 270)
                    angle = deg - 90;
                else if (deg >= 270 && deg <= 360)
                    angle = deg - 180;
                else
                    angle = deg;

                return angle;
            }
            case LOWER_LEFT -> {
                if (deg >= toRadians(0) && deg <= toRadians(90))
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
                if (deg >= toRadians(0) && deg <= toRadians(90))
                    angle = deg + 270;
                else if (deg >= 90 && deg <= 180)
                    angle = deg + 180;
                else if (deg >= 180 && deg <= 270)
                    angle = deg + 90;
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
        double theta;
        double r;
        double xTemp, yTemp;
        double x = 0, y = 0;

        r = R * Math.sqrt(random());    // https://stackoverflow.com/questions/5837572/generate-a-random-point-within-a-circle-uniformly
        theta = random() * 2 * Math.PI;

        switch (playerPosition) {
            case BOTTOM_LEFT -> theta = adjustDegree(theta, circlePart.UPPER_RIGHT);
            case TOP_LEFT -> theta = adjustDegree(theta, circlePart.LOWER_RIGHT);
            case TOP_RIGHT -> theta = adjustDegree(theta, circlePart.LOWER_LEFT);
            case BOTTOM_RIGHT -> theta = adjustDegree(theta, circlePart.UPPER_LEFT);
        }
        xTemp = r * cos(toRadians(theta));
        yTemp = r * sin(toRadians(theta));
        switch (playerPosition) {
            case BOTTOM_LEFT -> {
                x = xTemp;
                y = yTemp + mainBoard.getHeight();
            }
            case TOP_LEFT -> {
                x = xTemp;
                y = yTemp;
            }
            case TOP_RIGHT -> {
                x = xTemp + mainBoard.getWidth();
                y = yTemp;
            }
            case BOTTOM_RIGHT -> {
                x = xTemp + mainBoard.getWidth();
                y = yTemp + mainBoard.getHeight();
            }
        }
        Vector2D pos = new Vector2D(x, y);
        return pos;
    }

    /*
    ------------------------------------------------------------
    -------------- Initialization methods ----------------------
    ------------------------------------------------------------
    */

    private void initPositions() {
        ArrayList<Vector2D> StartingPositions = new ArrayList<>();
        switch (this.playerNum) {
            case 2: {
                StartingPositions.set(0, generateRandomPosition(playerPositions.TOP_LEFT));
                StartingPositions.set(1, generateRandomPosition(playerPositions.BOTTOM_RIGHT));
            }
            case 3: {
                StartingPositions.set(0, generateRandomPosition(playerPositions.TOP_LEFT));
                StartingPositions.set(0, generateRandomPosition(playerPositions.TOP_RIGHT));
                StartingPositions.set(0, generateRandomPosition(playerPositions.BOTTOM_LEFT));
            }
            case 4: {
                StartingPositions.set(0, generateRandomPosition(playerPositions.TOP_LEFT));
                StartingPositions.set(0, generateRandomPosition(playerPositions.TOP_RIGHT));
                StartingPositions.set(0, generateRandomPosition(playerPositions.BOTTOM_LEFT));
                StartingPositions.set(0, generateRandomPosition(playerPositions.BOTTOM_RIGHT));
            }
        }
        // Randomize starting positions between players
        Collections.shuffle(StartingPositions);
        for (int i = 0; i < Players.length; i++) {
            Players[i].setPosition(StartingPositions.get(i));
        }
    }

    private void initColors() {
        // Create a randomized list from the colors
        ArrayList<Color> Colors = new ArrayList<>();
        for (int i = 0; i < this.PlayerColors.length; i++) {
            Colors.set(i, PlayerColors[i]);
        }
        Collections.shuffle(Colors);
        // Assign the random color values to the players
        for (int i = 0; i < this.Players.length; i++) {
            this.Players[i].setPlayerColor(Colors.get(i));
        }
    }

    private void initBoard() {
        Color[] initColors = new Color[Players.length];
        CurvePoint[] initPoints = new CurvePoint[Players.length];
        Vector2D[] initPositions = new Vector2D[Players.length];
        for (int i = 0; i < Players.length; i++) {
            initColors[i] = Players[i].getPlayerColor();
            initPositions[i] = Players[i].getPosition();
            initPoints[i].setIsColored(true);
            initPoints[i].setCoordinates(initPositions[i].getX(), initPositions[i].getY());
        }
        mainBoard.addCurvePoints(initPoints);
        mainBoard.setCurveColors(initColors);
    }

    public void initGame() {
        initPositions();
        initColors();
        initBoard();
    }


    /*
    ------------------------------------------------------------
    -------------- Methods for game running --------------------
    ------------------------------------------------------------
    */

    public void updatePositions(ControlState[] Controls) {
        for (int i = 0; i < Players.length; i++) {
            Players[i].setControlState(Controls[i]);
            if (Players[i].getIsAlive())
                Players[i].move();
        }
    }

    public boolean[] detectCollisions() {
        boolean[] collisionDetected = new boolean[Players.length];
        // Initial value is false
        Arrays.fill(collisionDetected, false);

        Curve[] Curves;
        Curves = mainBoard.getCurves();
        CurvePoint currentPos;
        CurvePoint lastPos;
        CurvePoint curveSegment1;
        CurvePoint curveSegment2;
        for (int i = 0; i < Curves.length; i++) {   // Iterate over players
            // Store last two points of the actual player curve
            currentPos = Curves[i].getLastPoint();
            lastPos = Curves[i].getPoint(Curves[i].getCurveSize()-2);

            // Iterate over all of the curves on the board
            for (int j = 0; j < Curves.length; j++) {

                // Iterate over point pairs of the selected curve
                for (int k = 0; k < Curves[i].getCurveSize() - 1; k++) {

                    curveSegment1 = Curves[j].getPoint(k);
                    curveSegment2 = Curves[j].getPoint(k+1);
                    // Check if points are not in a hole in the curve
                    if (curveSegment1.getIsColored() && curveSegment2.getIsColored())
                        // Check intersection
                        if (doIntersect(currentPos, curveSegment1, lastPos, curveSegment2))
                            collisionDetected[i] = true;
                }
            }
        }

        return collisionDetected;
    }

    // Given three collinear points p, q, r, the function checks if
    // point q lies on line segment 'pr'
    static boolean onSegment(Vector2D p, Vector2D q, Vector2D r)
    {
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
    static int orientation(Vector2D p, Vector2D q, Vector2D r)
    {
        // See https://www.geeksforgeeks.org/orientation-3-ordered-points/
        // for details of below formula.
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0) return 0; // collinear

        return (val > 0)? 1: 2; // clock or counterclock wise
    }

    // The main function that returns true if line segment 'p1q1'
    // and 'p2q2' intersect.
    static boolean doIntersect(Vector2D p1, Vector2D q1, Vector2D p2, Vector2D q2)
    {
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

    public void evaluateStep(ControlState[] Controls) {
        boolean[] collisions = detectCollisions();
        for (int i = 0; i < Players.length; i++) {
            if (collisions[i]) {
                Players[i].setAlive(false);
            }
            if (Players[i].getIsAlive()) {
                Players[i].updateScore(SCORE_PER_TICK);
            }
        }
        updatePositions(Controls);
    }
}

