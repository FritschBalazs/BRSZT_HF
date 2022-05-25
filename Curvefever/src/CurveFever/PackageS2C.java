package CurveFever;

public class PackageS2C implements java.io.Serializable{
    public CurvePoint[] CurvePoints;
    public Vector2D prepSpeed;
    public int currentRound;
    public GameState gameState;
    public double[] Scores;  //TODO (B/M/D low prio) megegyszer rendberakni hogy int vagy double (Szerinetm maradhat mert tul sok helyen kene valtoztatni)

    public PackageS2C(int numOfPlayers) {
        CurvePoints = new CurvePoint[numOfPlayers];
        Scores = new double[numOfPlayers];
    }
}
