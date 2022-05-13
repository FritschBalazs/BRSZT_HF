package CurveFever;

import java.awt.*;

public class InitPackageS2C extends PackageS2C implements java.io.Serializable{
    public int playerID;
    public String[] playerNames;
    public int numOfRounds;

    public Color[] Colors;

    public InitPackageS2C (int numOfPlayers) {
        super(numOfPlayers);  //constructor for the superclass (PackageS2C in this case)
        playerNames = new String[numOfPlayers];
        Colors = new Color[numOfPlayers];
    }
}
