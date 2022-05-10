package CurveFever;

public class Menu {
    private Client client;
    private Server server; //only one Server or one Client will be used
    private MenuState menuState;
    private ControlOptions controlOptions;
    private String serverIp; //To give to Server or Client constructor
    private String playerName; //To give to Player constructor (player name can be entered in menu, in a textbox)
    private int numOfPlayers;
    private int numOfRounds;

    public Menu(){}

    public void setMenuState(MenuState menuState) {this.menuState = menuState;}
    public void setControlOptions(ControlOptions controlOptions) {this.controlOptions = controlOptions;}
    public void setServerIp(String serverIp) {this.serverIp = serverIp;}
    public void setPlayerName(String playerName) {this.playerName = playerName;}
    public void setNumOfPlayers(int numOfPlayers) {this.numOfPlayers = numOfPlayers;}
    public void setNumOfRounds(int numOfRounds) {this.numOfRounds = numOfRounds;}
    public Client getClient() {return client;}
    public Server getServer() {return server;}
    public MenuState getMenuState() {return menuState;}
    public ControlOptions getControlOptions() {return controlOptions;}
    public String getServerIp() {return serverIp;}
    public String getPlayerName() {return playerName;}
    public int getNumOfPlayers() {return numOfPlayers;}
    public int getNumOfRounds() {return numOfRounds;}


    public void createGame(){
        //Start game in Server mode
    }

    public void joinGame(){
        //Start game in Client mode
    }

    public void updateMenu(){
        //Controlling menu logic (big while loop)
    }

    public void renderMenu(){
        //Drawing Menu
    }
}
