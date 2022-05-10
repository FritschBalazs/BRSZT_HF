package CurveFever;

public class Client {
    private Player player;
    private Board board;
    private boolean isGameHost;
    private String serverIp;

    //For client
    public Client(String serverIp, String playerName, boolean isGameHost) {
        if (isGameHost) {
            this.serverIp = "localhost";
        } else {
            this.serverIp = serverIp;
        }
        this.player = new Player(playerName);

    }


    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setGameHost(boolean isGameHost) {
        this.isGameHost = isGameHost;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Player getPlayer() {
        return player;
    }

    public Board getBoard() {
        return board;
    }

    public boolean getIsGameHost() {
        return isGameHost;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void updateBoard() {

    }

    public void sendToServer() {

    }

    public PackageS2C receiveFromServer() {
        return null;
    }

    public InitPackageS2C receiveFromServerInit() {
        return null;
    }
}
