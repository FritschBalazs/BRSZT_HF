package CurveFever;

import CurveFever.gui.GameScreen;
import CurveFever.gui.MenuScreen;

import javax.swing.*;
import java.awt.*;

public class GUITest {
    public static void main(String[] args) {
        //TESTING GAMESCREEN
        Curve curve = Main.generateRandomCurve(500,0, 720, Color.PINK, 70);
        Curve curve1 = Main.generateRandomCurve(500,0, 720, Color.BLUE, 70);
        Curve curve2 = Main.generateRandomCurve(500,0, 720, Color.RED, 70);
        Curve curve3 = Main.generateRandomCurve(500,0, 720, Color.GREEN, 70);
        JFrame window = new JFrame("Kurve Fívör gui");
        //window.setSize(2000,1500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // when we close the window, stop the app
        Board board = new Board(4,5,new String[]{"Hanti","Dani", "Frici","valaki"},new Color[]{Color.PINK,Color.PINK,Color.PINK,Color.PINK});
        board.setCurves(new Curve[]{curve, curve1, curve2, curve3}); //setting the random curves
        GameScreen gamescreen = new GameScreen(4);
        gamescreen.setCurrentRound(board.getCurrentRound());
        gamescreen.setPlayerNames(board.getPlayerNames());
        gamescreen.setCurves(board.getCurves());
        gamescreen.setPoints(board.getPoints());
        gamescreen.setNumOfPlayers(4);
        double[] points = new double[4];
        for (int i = 0; i < 4; i = i+1) {
            points[i] = i;
        }
        gamescreen.setPoints(points);
        window.add(gamescreen,BorderLayout.CENTER);

        //TESTING MENUSCREEN
        MenuScreen menuscreen = new MenuScreen();
        //window.add(menuscreen);


        //window.setResizable(false); // don't allow the user to resize the window
        ImageIcon logo = new ImageIcon("src/curvefeverlogo.jpg");
        window.setIconImage(logo.getImage()); //adding logo to window
        window.pack(); // fit the window size around the components (just our jpanel).

        window.setLocationRelativeTo(null); // open window in the center of the screen
        gamescreen.render();
        window.setVisible(true); // display the window
    }
}
