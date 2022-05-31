package CurveFever;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client{
    protected Player player;
    protected Board board;
    private String serverIp;

    private Socket socket;
    private ObjectInputStream objIStream;
    private ObjectOutputStream objOStream;

    protected static final int port = 7785;


    public Client(String serverIp, String playerName) {

        this.serverIp = serverIp;
        this.player = new Player(playerName);
    }


    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setBoard(Board board) {
        this.board = board;
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

    public String getServerIp() {
        return serverIp;
    }

    public void updateBoard() {

    }

    public void receiveFromServerInit(){

        /* Connect to server*/
        establishConnection();

        try {
            /* wait for signal */
            String msg = objIStream.readUTF();
            System.out.println("Message from server: "+msg);

            /* Send player name */
            objOStream.writeUTF(player.getName());
            objOStream.flush();

            /* Wait for init package */
            InitPackageS2C pkg;
            pkg = (InitPackageS2C) objIStream.readObject();

            /* save init data */
            player.setId(pkg.playerID);
            player.setName(pkg.playerNames[pkg.playerID]);

            /* create the board */
            board = new Board(pkg);


        } catch (IOException e) {
            System.out.println("IOException from sendToServerInit");
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException from sendToServerInit");
        }
    }

    public boolean sendToServer() {

        String requestMessage = "";
        boolean retval = true;
        /* if the connection got closed, or it has never been started */
        if (socket == null || socket.isClosed() ) {
            establishConnection();
        }

        /* wait for server to request input */
        try {
            requestMessage = objIStream.readUTF();
        } catch (IOException e) {
            System.out.println("IOException when waiting for server to request input");
        }

        if (requestMessage.equals("Provide control input pls") )
        {
            /* send controlState to server */
            try {
                objOStream.writeObject(player.getControlState());
                objOStream.flush();
            } catch (IOException e) {
                System.out.println("IOexception while trying to send");
            }
            System.out.println("Contorl state sent to server.");

            retval = true;
        }
        else if (requestMessage.equals("Game over")) {
            retval = false;
        }

       return retval;

    }

    public PackageS2C receiveFromServer() {
        /* if the connection got closed, or it has never been started */
        if (socket == null || socket.isClosed() ) {
            establishConnection();
        }

        try {
            PackageS2C message  = (PackageS2C) objIStream.readObject();
            System.out.println("Game data pacakge received from server No."+message.currentRound);
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

        int cnt = 0;

        System.out.println("Connecting to server at " + serverIp + " ");
        while(objIStream == null) {
            try {
                socket = new Socket(InetAddress.getByName(serverIp), port);
                InputStream iStream = socket.getInputStream();
                OutputStream oStream = socket.getOutputStream();

                objIStream = new ObjectInputStream(iStream);
                objOStream = new ObjectOutputStream(oStream);

                System.out.println("Connection to server complete");

            } catch (UnknownHostException e) {
                System.out.println("Server IP not valid");
            } catch (IOException e) {
                /* if server is localhost, and not running yet */
                if (  e.getMessage().equals("Connection refused") ) {
                    cnt++;
                    if (cnt % 10000 == 0){
                        System.out.print(".");
                    }
                } else {
                    System.out.println("IOException when trying to connect: " + e.getMessage());
                }
            }

        }


    }
}
