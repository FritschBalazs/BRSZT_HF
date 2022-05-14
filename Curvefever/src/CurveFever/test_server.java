package CurveFever;

import java.awt.*;
import java.util.Random;

public class test_server {


    public static void main(String[] args){


        Server myServer = new Server(4,4,"Szerver pisti");

        myServer.acceptConnections();
        myServer.setupGame();



        while (true);

    }


}
