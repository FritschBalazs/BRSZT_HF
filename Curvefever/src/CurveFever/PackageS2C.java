package CurveFever;

public class PackageS2C implements java.io.Serializable{
    public CurvePoint[] CurvePoints;
    public int currentRound;
    public GameState gameState;
    public int[] Scores;

    public PackageS2C(int numOfPlayers) {
        CurvePoints = new CurvePoint[numOfPlayers];
        Scores = new int[numOfPlayers];
    }
}
