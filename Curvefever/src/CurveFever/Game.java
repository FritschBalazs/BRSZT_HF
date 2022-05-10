package CurveFever;

public class Game {
    private int playerNum;
    private ServerSidePlayer[] Players;
    private int roundNum;
    private int currentRound;
    private Board mainBoard;
    private GameState gameState;

    public Game(ServerSidePlayer Players[]) {
        this.roundNum = 3;
        this.playerNum = 3;
        this.currentRound = 0;
        this.mainBoard = new Board(100, 100, this.playerNum);
        for (int i = 0; i < this.playerNum; i++) {
            this.Players[i] = Players[i];
        }

    }

    public Game(int playerNum, int roundNum, ServerSidePlayer[] Players){
        if (roundNum > 0 && roundNum < 10)
            this.roundNum = roundNum;
        else
            this.roundNum = 3;

        this.playerNum = Players.length;

        this.currentRound = 0;

        for (int i = 0; i < Players.length; i++) {
            this.Players[i] = Players[i];
        }


        this.mainBoard = new Board(100, 100, playerNum);

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

    public void updatePositions() {

    }

    public void evaluateStep() {

    }

    public void setInputs() {

    }
}
