package CurveFever;

public class PackageS2C {
    public CurvePoint[] CurvePoints;
    public int currentRound;
    public GameState gameState;
    public double[] Scores;

    public PackageS2C(int numOfPlayers) {
        CurvePoints = new CurvePoint[numOfPlayers];
        Scores = new double[numOfPlayers];
    }

    //TODO ide jo ha public minden, es nincs setter meg getter vagy ilyet nem illik?

}
