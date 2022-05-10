package CurveFever;

public class InitPackageS2C extends PackageS2C implements java.io.Serializable{
    public int playerID;
    public String[] playerNames;
    public int numOfRounds;

    public InitPackageS2C (int numOfPlayers) {
        super(numOfPlayers);  //constructor for the superclass (PackageS2C in this case)
        playerNames = new String[numOfPlayers];
    }

    //TODO ahogy dontottunk PackageS2C-ben ugyanazt alkalmazni ide is
}
