package CurveFever;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Client{

    private Game game;
    private int numOfClients;   //NOT the number of players!!!!!!!!
    private ServerSocket ss;
    private Socket[] Sockets;
    private ObjectOutputStream[] ObjOutStreams;
    private ObjectInputStream[]  ObjInStreams;
    private GetControl[] GCRunnables;
    private SendPoints[] SPRunnables;


    //private int port;

    public Server(int numOfPlayers, String playerName) {
        super("", playerName, true);

        numOfClients = numOfPlayers - 1;
        //this.port = port;

        try {
            ss = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("IOException from server constructor");
        }
        System.out.println("----- Server created -----");
    }

    public void setClientIpAddresses(String[] addresses) {
        this.ClientIpAddresses = addresses;
    }

    //not sure if this is needed, or correct
    public void setGame(Game game) {
        this.game = game;
    }

    /*public void setPort (int port) {
        this.port = port;
    }*/

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

    public void acceptConnections()
    {
        int numOfPlayer = 0;

        System.out.println("Waiting for connections.... (1/"+(numOfClients+1)+")";
        try {
            while (numOfPlayer < numOfClients) {
                /* accept new connection */
                Sockets[numOfPlayer] = ss.accept();

                /* store IO streams, and create runnables */
                ObjOutStreams[numOfPlayer] = new ObjectOutputStream(Sockets[numOfPlayer].getOutputStream());
                ObjInStreams[numOfPlayer] = new ObjectInputStream(Sockets[numOfPlayer].getInputStream());

                SPRunnables[numOfPlayer] = new SendPoints(numOfPlayer);
                GCRunnables[numOfPlayer] = new GetControl(numOfPlayer);

                /* increment player number */
                numOfPlayer++;
                System.out.println("New player connected. "  + numOfPlayer+ "/" + (numOfClients+1)) ;
            }
            System.out.println("All " + numOfClients + 1 + " players connected");
        } catch (IOException ex) {
            System.out.println("IOException from acceptConnections()");
        }

    }

    public void setupGame(){
        //TODO blokkolva elküldi a dolgokat.
    }


    public void sendToClient(boolean InitPackage) {
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

    public void requestInputs() {

    }

    public void run() {

    }

    /* private halper classes */
    private class GetControl implements Runnable {

        private final int clientId;



        public GetControl(int clientId){

            this.clientId = clientId;
            System.out.println("GetControl runnable for client #" + clientId + "created");
        }
        public void run() {

            try {

                /* send out a request */
                ObjOutStreams[clientId].writeUTF("Provide control input pls");
                ObjOutStreams[clientId].flush();

                //TODO megcsinalni hogy a thread a tenyleges valtozot allitsa
                ControlState dummy;
                dummy = (ControlState)ObjInStreams[clientId].readObject();
            } catch (IOException e) {
                System.out.println("IOException from getControl runnable #" + clientId);
            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException from getControl runnable #" + clientId);
            }
        }
    }

    private class SendPoints implements Runnable {

        private final int clientId;

        public SendPoints(int clientId) {
            this.clientId = clientId;
            System.out.println("SendPoints runnable for client #" + clientId + "created");
        }

        public void run() {

            try {
                //TODO megcsinalni hogy a tenyleges adatot kuldje
                PackageS2C dummyForClient = new PackageS2C(4);
                ObjOutStreams[clientId].writeObject(dummyForClient);
            } catch (IOException e) {
                System.out.println("IOException from SendPoints runnable #" + clientId);
            }
        }
    }
}
