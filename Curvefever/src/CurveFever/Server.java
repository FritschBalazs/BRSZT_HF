package CurveFever;

public class Server extends Client{

    private Game game;
    private String[] ClientIpAddresses;
    private int port;

    public Server(int port, String playerName) {
        super("", playerName, true);

        this.port = port;
    }

    public void setClientIpAddresses(String[] addresses) {
        this.ClientIpAddresses = addresses;
    }

    //not sure if this is needed, or correct
    public void setGame(Game game) {
        this.game = game;
    }

    public void setPort (int port) {
        this.port = port;
    }

    public Game getGame() {
        return game;
    }

    public String[] getClientIpAddresses(){
        return ClientIpAddresses;
    }


    public String getAClientIpAddress(int n){
        return ClientIpAddresses[n];
    }

    public int getPort() {
        return port;
    }


    public void sendToClient(boolean InitPackage) {

    }

    public void requestInputs() {

    }

    public void run() {

    }
}
