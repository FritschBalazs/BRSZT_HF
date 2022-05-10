package CurveFever;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client{
    private Player player;
    private Board board;
    private boolean isGameHost;
    private String serverIp;

    private Socket socket;
    private ObjectInputStream objIStream;
    private ObjectOutputStream objOStream;

    protected static final int port = 7785;


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

        /* if the connection got closed, or it has never been started */
        if (socket == null || socket.isClosed() ) {
            establishConnection();
        }

        try {
            objOStream.writeObject(player.getControlState());
            objOStream.flush();
        } catch (IOException e) {
            System.out.println("IOexception while trying to send");
        }

    }

    public PackageS2C receiveFromServer() {
        /* if the connection got closed, or it has never been started */
        if (socket == null || socket.isClosed() ) {
            establishConnection();
        }

        try {
            PackageS2C message  = (PackageS2C) objIStream.readObject();
            return message;
        } catch (IOException e) {
            System.out.println("IOexception while trying to receive");
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException");
        }


        /* if we get to here that's an error */
        return null;
    }

    public InitPackageS2C receiveFromServerInit() {
        /* if the connection got closed, or it has never been started */
        if (socket == null || socket.isClosed() ) {
            establishConnection();
        }

        try {
            InitPackageS2C message  = (InitPackageS2C) objIStream.readObject();
            return message;
        } catch (IOException e) {
            System.out.println("IOexception while trying to receive");
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException");
        }


        /* if we get to here that's an error */
        return null;
    }


    private void establishConnection() {

        //TODO port megadast kitalalni? külön megadni, kiszedni a stringnbol vagy hardcode?

        System.out.println("Connecting to server...");
        try {
            socket = new Socket(InetAddress.getByName(serverIp),port);
            InputStream iStream = socket.getInputStream();
            OutputStream oStream = socket.getOutputStream();

            objIStream = new ObjectInputStream(iStream);
            objOStream = new ObjectOutputStream(oStream);

        } catch (UnknownHostException e) {
            System.out.println("Server IP not valid");
        } catch (IOException e) {
            System.out.println("IOException when trying to connect");
        }

        System.out.println("Connected to server successful");
    }
}
