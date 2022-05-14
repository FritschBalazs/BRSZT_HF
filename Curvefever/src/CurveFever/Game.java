package CurveFever;

import java.awt.*;
import java.util.ArrayList;

public class Game {
    public static final int MAX_PLAYERS = 4;
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_ROUNDS = 10;
    public static final int MIN_ROUNDS = 1;
    public static final int SCORE_PER_SECOND = 50;
    public static final int SYSTEM_TICK = 10;
    public static final double SCORE_PER_TICK = (double)SCORE_PER_SECOND / (double)SYSTEM_TICK;

    private int playerNum;
    private ServerSidePlayer[] Players;
    private int roundNum;
    private int currentRound;
    private Board mainBoard;
    private GameState gameState;

    public Game(ServerSidePlayer[] Players) {
        this.roundNum = 3;
        this.playerNum = 3;
        this.currentRound = 0;
        //this.mainBoard = new Board(100, 100, this.playerNum);
        this.Players = Players.clone();
        this.gameState = GameState.MENU;

    }

    public Game(int playerNum, int roundNum, ServerSidePlayer[] Players, Color[] Colors){
        if (roundNum >= MIN_ROUNDS && roundNum < MAX_ROUNDS)
            this.roundNum = roundNum;
        else
            this.roundNum = 3;  // TODO Add error handling

        if (Players.length <= MAX_PLAYERS && Players.length >= MIN_PLAYERS)
            this.playerNum = Players.length;
        else
            this.playerNum = 2;/*TODO error handling*/

        this.currentRound = 0;
        this.Players = Players.clone();
        //this.mainBoard = new Board(100, 100, playerNum);
        this.gameState = GameState.MENU;

        String[] playerNames = new String[this.playerNum];
        for (int i = 0; i < this.playerNum; i++) {
            playerNames[i] = Players[i].getName();
        }
        this.mainBoard = new Board(this.playerNum,this.roundNum,playerNames,Colors);
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

    public void updatePositions(ControlState... Controls) {
        for (int i = 0; i < Players.length; i++){
            Players[i].setControlState(Controls[i]);
            if (Players[i].getIsAlive())
                Players[i].move();
        }
    }

    public boolean[] detectCollisions() {
        boolean[] collisionDetected = new boolean[Players.length];
        for (int i = 0; i < Players.length; i++) {   // No collisions at the beginning
            collisionDetected[i] = false;
        }
        /* TODO Detect collision part of the method*/
        return collisionDetected;
    }
    public void evaluateStep(ControlState[] Controls) {
        boolean[] collisions = detectCollisions();
        for (int i = 0; i < Players.length; i++) {
            if (collisions[i]) {
                Players[i].setAlive(false);
            }
            if (Players[i].getIsAlive()){
                Players[i].updateScore(SCORE_PER_TICK);
            }
        }
        updatePositions(Controls);
    }
}
