package CurveFever;

public class Main {

    /* ez csak random cucc nyugodtan torold */
    public static void main(String[] args) {
        Client myClient = new Client("localhost","en",false);
        myClient.receiveFromServer();
    }
}