package CurveFever;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class test_client_1 {


    public static void main(String[] args){



        Client myClient = new Client("localhost","en",false);
        Player myPlayer = new Player("1_sanyi");
        myClient.setPlayer(myPlayer);
        myClient.receiveFromServerInit();

        JFrame window = new JFrame("Kurve Fívör");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // when we close the window, stop the app
        Board board = myClient.getBoard();
        window.add(board); // add the jpanel to the window
        window.setResizable(false); // don't allow the user to resize the window
        ImageIcon logo = new ImageIcon("src/curvefeverlogo.jpg");
        window.setIconImage(logo.getImage());
        window.pack(); // fit the window size around the components (just our jpanel).
        window.setLocationRelativeTo(null); // open window in the center of the screen
        //board.setCurves(new Curve[]{curve, curve1, curve2, curve3}); //setting the random curves
        board.repaint(); //repainting the board
        window.setVisible(true); // display the window

        while (true) {
            myClient.sendToServer();
            PackageS2C message = myClient.receiveFromServer();
            if (message != null )  {
                board.receiveFromPackageS2C(message);
            }
            board.repaint();
        }

    }

}
