package CurveFever;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

public class Game {
    public class InitVector{
        public Vector2D pos;
        public Vector2D speed;

        public InitVector(Vector2D pos, Vector2D speed){
            this.pos = new Vector2D(pos.getX(), pos.getY());
            this.speed = new Vector2D(speed.getX(), speed.getY());
        }

        public void setValues(Vector2D pos, Vector2D speed) {
            this.pos.setCoordinates(pos.getX(), pos.getY());
            this.speed.setCoordinates(speed.getX(), speed.getY());
        }
    }
    private enum playerPositions {
        BOTTOM_LEFT,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT
    }

    private static final java.awt.Color[] PlayerColors = {
            Color.RED, Color.ORANGE, Color.PINK, Color.GREEN, Color.YELLOW, Color.CYAN};
    private static final double R = 200.0;
    public static final int MAX_PLAYERS = 4;
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_ROUNDS = 10;
    public static final int MIN_ROUNDS = 1;
    public static final int SCORE_PER_SECOND = 5;
    public static final int SYSTEM_TICK = ServerSidePlayer.SYSTEM_TICK;
    public static final double SCORE_PER_TICK = (double) SCORE_PER_SECOND / (double) SYSTEM_TICK;
    public static final double SPEED_UPSCALE = 10;
    public static final double REWARD_SCORE = 100;
    public static final int INIT_POS_BORDER = 200;

    private int playerNum;
    private ServerSidePlayer[] Players;
    private int roundNum;
    private int currentRound;
    private Board mainBoard;
    private GameState gameState;

    private double[] roundScores;
    private int[] deadIndexes;

    /*
    ---------------------------------------------------------------------------
    ------------------- Constructors, getters, setters ------------------------
    ---------------------------------------------------------------------------
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

        this.roundScores = new double[this.playerNum];
        this.deadIndexes = new int[this.playerNum];
        Arrays.fill(roundScores, 0);
        Arrays.fill(deadIndexes, -1);

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
    ----------------------------------------------------------------------
    ----------------- Math for game logic and init -----------------------
    ----------------------------------------------------------------------
    */

    public Vector2D generateRandomPosition(playerPositions playerPosition) {
        double theta, theta_temp;
        double r;
        double xTemp, yTemp;
        double x = 0, y = 0;
        // Generate random values
        r = R * Math.sqrt(random()) + 50;    // https://stackoverflow.com/questions/5837572/generate-a-random-point-within-a-circle-uniformly
        theta = random() * 2 * Math.PI;
        theta_temp = toDegrees(theta);
        //System.out.println("theta_temp: " + theta_temp + ", player position: " + playerPosition);
        xTemp = r * cos(toRadians(theta));
        yTemp = r * sin(toRadians(theta));

        // Adjust starting positions if they are too close to the border
        if (xTemp < INIT_POS_BORDER && xTemp > 0)
            xTemp = INIT_POS_BORDER;
        else if (xTemp > -INIT_POS_BORDER && xTemp < 0)
            xTemp = -INIT_POS_BORDER;

        if (yTemp < INIT_POS_BORDER && yTemp > 0)
            yTemp = INIT_POS_BORDER;
        else if (yTemp > -INIT_POS_BORDER && yTemp < 0)
            yTemp = -INIT_POS_BORDER;

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
        /*System.out.println("theta: " + theta + ", r: " + r);
        System.out.println("x: " + x + " y: " + y);
        System.out.println("--------------------------------------");*/
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

    public boolean[] setHoles(int currentCycle) {
        boolean[] temp = new boolean[playerNum];
        for (int i = 0; i < playerNum; i++) {
            temp[i] = mainBoard.getCurves()[i].getLastPoint().getIsColored();
            if (currentCycle >= Players[i].cycle) {
                // skip
                if (temp[i]) {
                    Players[i].cycle += 5;
                    temp[i] = false;
                }
                else {
                    Players[i].cycle += 80 + 10 * Math.random();
                    temp[i] = true;
                }
            }
        }
        return temp;
    }

    // Use function on temporary arrays
    public void updatePlayerScores(){
        // Store alive player as "last dead"
        for (int i = 0; i < playerNum; i++) {
            if (Players[i].getIsAlive())
                deadIndexes[playerNum-1] = i;
        }
        double[] tempScores = roundScores.clone();
        Arrays.sort(tempScores);
        double max = tempScores[playerNum-1];
        double tempDouble = 0.0;
        // Iterate over players to search for the corresponding score
        for (int i = 0; i < playerNum; i++) {
            tempDouble = 0.3 * max / pow(2, (playerNum-1-i));
            Players[deadIndexes[i]].updateScore(floor(tempDouble));
        }

        // Set scores on the game board to display correctly
        double[] scoresToBoard = new double[playerNum];
        for (int i = 0; i < playerNum; i++) {
            scoresToBoard[i] = Players[i].getScore();
        }
        mainBoard.setScores(scoresToBoard);
    }

    /*
    ----------------------------------------------------------------------
    ------------------- Initialization methods ---------------------------
    ----------------------------------------------------------------------
    */

    public void initPositions() {
        ArrayList<InitVector> StartingPositions = new ArrayList<>(this.playerNum);
        Vector2D tempPos = new Vector2D();
        Vector2D tempSpeed = new Vector2D();
        InitVector tempInit = new InitVector(tempPos,tempSpeed);
        switch (this.playerNum) {
            case 2: {
                tempPos = generateRandomPosition(playerPositions.TOP_LEFT);
                tempSpeed.setCoordinates(2, 2);
                tempInit.setValues(tempPos, tempSpeed);
                StartingPositions.add(0, new InitVector(tempPos, tempSpeed));

                tempPos = generateRandomPosition(playerPositions.BOTTOM_RIGHT);
                tempSpeed.setCoordinates(-2,-2);
                tempInit.setValues(tempPos, tempSpeed);
                StartingPositions.add(1, new InitVector(tempPos, tempSpeed));
            }
            break;
            case 3: {
                tempPos = generateRandomPosition(playerPositions.TOP_LEFT);
                tempSpeed.setCoordinates(2, 2);
                tempInit.setValues(tempPos, tempSpeed);
                StartingPositions.add(0, new InitVector(tempPos, tempSpeed));

                tempPos = generateRandomPosition(playerPositions.BOTTOM_RIGHT);
                tempSpeed.setCoordinates(-2,-2);
                tempInit.setValues(tempPos, tempSpeed);
                StartingPositions.add(1, new InitVector(tempPos, tempSpeed));

                tempPos = generateRandomPosition(playerPositions.BOTTOM_LEFT);
                tempSpeed.setCoordinates(2,-2);
                tempInit.setValues(tempPos, tempSpeed);
                StartingPositions.add(2, new InitVector(tempPos, tempSpeed));
            }
            break;
            case 4: {
                tempPos = generateRandomPosition(playerPositions.TOP_LEFT);
                tempSpeed.setCoordinates(2, 2);
                tempInit.setValues(tempPos, tempSpeed);
                StartingPositions.add(0, new InitVector(tempPos, tempSpeed));

                tempPos = generateRandomPosition(playerPositions.BOTTOM_RIGHT);
                tempSpeed.setCoordinates(-2,-2);
                tempInit.setValues(tempPos, tempSpeed);
                StartingPositions.add(1, new InitVector(tempPos, tempSpeed));

                tempPos = generateRandomPosition(playerPositions.BOTTOM_LEFT);
                tempSpeed.setCoordinates(2,-2);
                tempInit.setValues(tempPos, tempSpeed);
                StartingPositions.add(2, new InitVector(tempPos, tempSpeed));

                tempPos = generateRandomPosition(playerPositions.TOP_RIGHT);
                tempSpeed.setCoordinates(-2,2);
                tempInit.setValues(tempPos, tempSpeed);
                StartingPositions.add(3, new InitVector(tempPos, tempSpeed));
            }
            break;
        }
        // Randomize starting positions between players
        Collections.shuffle(StartingPositions);
        for (int i = 0; i < Players.length; i++) {
            Players[i].setPosition(StartingPositions.get(i).pos);
            Players[i].setSpeed(StartingPositions.get(i).speed);
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
            //System.out.println("Players[" + i + "] : x: " + Players[i].getPosition().getX() + "; y: " + Players[i].getPosition().getY());
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
    ----------------------------------------------------------------------
    ------------------- Methods for game running -------------------------
    ----------------------------------------------------------------------
    */

    public void updatePositions(ControlState[] Controls, int currentCycle) {
        // Get player states
        boolean[] playersAlive = new boolean[playerNum];
        for (int i = 0; i < playerNum; i++) {
            playersAlive[i] = Players[i].getIsAlive();
        }
        Vector2D pos = new Vector2D(0,0);
        CurvePoint newPosition;
        // Check collisions of alive players
        boolean[] collisions = detectCollisions(playersAlive);
        boolean[] isColored = setHoles(currentCycle);
        // Set parameters according to collision event
        for (int i = 0; i < playerNum; i++) {
            Players[i].setControlState(Controls[i]);
            if (collisions[i]) {
                Players[i].setAlive(false);
                System.out.println("Collision detected, Player ID: " + i);
                this.deadIndexes[this.playerNum-countAlivePlayers()-1] = i;
            } else if (Players[i].getIsAlive()){
                Players[i].move();
                pos = Players[i].getPosition();
                newPosition = new CurvePoint(pos.getX(), pos.getY(), isColored[i]);
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
        CurvePoint currentPos;
        CurvePoint lastPos;

        Vector2D playerPos = new Vector2D();
        // Check if out of board boundaries
        for (int i = 0; i < playerNum; i++) {
            if (playersAlive[i] == true) {
                playerPos.setCoordinates(Players[i].getPosition().getX(), Players[i].getPosition().getY());

                if ((playerPos.x > mainBoard.getGameWidthWidth()) || (playerPos.x < 0)
                        || (playerPos.y > mainBoard.getGameHeightHeight()) || (playerPos.y < 0))
                    collisionDetected[i] = true;
            }
        }

        // Variables to store the point pairs in the Curves
        CurvePoint curveSegment1;
        CurvePoint curveSegment2;
        for (int i = 0; i < playerNum; i++) {   // Iterate over players
            if (playersAlive[i] == true && !collisionDetected[i])
                if (Curves[i].getCurveSize() > 4) {
                    // Store last two points of the actual player curve
                    currentPos = Curves[i].getLastPoint();
                    lastPos = Curves[i].getPoint(Curves[i].getCurveSize() - 2);

                    // Iterate over all of the curves on the board
                    for (int j = 0; j < playerNum; j++) {

                        // Iterate over point pairs of the selected curve
                        for (int k = 0; k < Curves[j].getCurveSize() - 2; k++) {
                            // Avoid false detection of the last curve point (actual position)
                            if (!((i == j) && (k >= Curves[j].getCurveSize() - 4))) {
                                curveSegment1 = Curves[j].getPoint(k);
                                curveSegment2 = Curves[j].getPoint(k + 1);
                                // Check if points are not in a hole in the curve
                                if (curveSegment1.getIsColored() && curveSegment2.getIsColored())
                                    // Check intersection
                                    if (doIntersect(currentPos, lastPos, curveSegment1, curveSegment2)) {
                                        collisionDetected[i] = true;
                                        System.out.println("Player[" + i + "] collision detected, coordinates:");
                                        System.out.println("currentPos -- x: " + currentPos.getX() + " y: " + currentPos.getY());
                                        System.out.println("lastPos -- x: " + lastPos.getX() + " y: " + lastPos.getY());
                                        System.out.println("Segment1 -- x: " + curveSegment1.getX() + " y: " + curveSegment1.getY());
                                        System.out.println("Segment2 -- x: " + curveSegment2.getX() + " y: " + curveSegment2.getY());
                                    }
                            }
                        }
                    }
                }
        }
        return collisionDetected;
    }

    public boolean evaluateStep(ControlState[] Controls, int currentCycle) {
        boolean endgame;
        double tmpScore[] = new double[playerNum];
        Arrays.fill(tmpScore, 0.0);
        for (int i = 0; i < playerNum; i++) {
            if (Players[i].getIsAlive()) {
                Players[i].updateScore(SCORE_PER_TICK);
                this.roundScores[i] += SCORE_PER_TICK;
            }
            tmpScore[i] = Players[i].getScore();
        }
        updatePositions(Controls, currentCycle);
        mainBoard.setScores(tmpScore);
        endgame = (countAlivePlayers() == 1);
        return endgame;
    }

    public boolean runGame(ControlState[] Controls, int currentCycle) {
            // Run game state machine
            switch (this.gameState) {
                // Set starting speed before game starts
                case PREP -> {
                    // Set speed attributes of the players according to the input controls
                    double angle;
                    for (int i = 0; i < playerNum; i++) {
                        Players[i].setControlState(Controls[i]);
                        if (Controls[i] == ControlState.LEFT)
                            angle = -1 * ServerSidePlayer.TURN_DEGREE_PER_TICK;
                        else if (Controls[i] == ControlState.RIGHT)
                            angle = ServerSidePlayer.TURN_DEGREE_PER_TICK;
                        else angle = 0;
                        Players[i].rotateSpeed(angle);
                    }

                    // Store visual speed in the board per player
                    CurvePoint[] tempSpeed = new CurvePoint[playerNum];
                    for (int i = 0; i < playerNum; i++) {
                        tempSpeed[i] = new CurvePoint();
                        tempSpeed[i].setCoordinates(Players[i].getSpeed().getX() * SPEED_UPSCALE + Players[i].getPosition().getX(),
                                Players[i].getSpeed().getY() * SPEED_UPSCALE + Players[i].getPosition().getY());
                        tempSpeed[i].setIsColored(true);

                        // Add new speed to the board if first cycle
                        if (mainBoard.getCurves()[i].getPoints().size() == 1)
                            mainBoard.getCurves()[i].addPoint(tempSpeed[i]);
                        else if (mainBoard.getCurves()[i].getPoints().size() == 2) {
                            mainBoard.getCurves()[i].setAPoint(1,tempSpeed[i]);
                        }
                    }
                    return false;

                }

                case PLAYING -> {
                    if (evaluateStep(Controls, currentCycle))
                        return true;
                }

                case MENU -> {
                    return false;
                }
            }
        return false;
    }
}


