package CurveFever;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {
    private static void initWindow() {
        // create a window frame and set the title in the toolbar
        JFrame window = new JFrame("Kurve Fívör");
        // when we close the window, stop the app
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // create the jpanel to draw on.
        // this also initializes the game loop
        Board board = new Board(3,5,new String[]{"Hanti", "Dani", "Frici"});
        // add the jpanel to the window
        window.add(board);
        // pass keyboard inputs to the jpanel
        //window.addKeyListener(board);

        // don't allow the user to resize the window
        window.setResizable(false);
        // fit the window size around the components (just our jpanel).
        // pack() should be called after setResizable() to avoid issues on some platforms
        window.pack();
        // open window in the center of the screen
        window.setLocationRelativeTo(null);
        // display the window
        window.setVisible(true);
    }

    private static final Color BLACK = new Color(0,0,0);
    private static final Color PINK = new Color(255,105,180);
    private static final Color BLUE = new Color(0,255,255);
    private static final Color RED = new Color(255,0,0);
    private static final Color GREEN = new Color(0,255,0);
    private static Curve generateRandomCurve(int numOfPoints, int rangeMin, int rangeMax, Color color, float percentOfIsColored){
        percentOfIsColored = percentOfIsColored/100;
        Curve curve = new Curve();
        curve.setColor(color);
        for (int i = 0; i < numOfPoints; i = i + 1) {
            Random x = new Random();
            double randomx = rangeMin + (rangeMax - rangeMin) * x.nextDouble();
            Random y = new Random();
            double randomy = rangeMin + (rangeMax - rangeMin) * y.nextDouble();
            Random help = new Random();
            boolean isColoredWProbability = (help.nextFloat() < percentOfIsColored);
            System.out.println(isColoredWProbability);
            CurvePoint randomCurvePoint = new CurvePoint(randomx, randomy, isColoredWProbability);
            curve.addPoint(randomCurvePoint);
        }
        return curve;
    }

    public static void main(String[] args) {
        //generating some random curves
        Curve curve = generateRandomCurve(100,0, 500, PINK, 70);
        Curve curve1 = generateRandomCurve(50,0, 400, BLUE, 70);
        Curve curve2 = generateRandomCurve(50,0, 350, RED, 70);
        Curve curve3 = generateRandomCurve(50,0, 300, GREEN, 70);


        JFrame window = new JFrame("Kurve Fívör");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // when we close the window, stop the app
        Board board = new Board(4,5,new String[]{"Hanti"});
        window.add(board); // add the jpanel to the window
        window.setResizable(false); // don't allow the user to resize the window
        window.pack(); // fit the window size around the components (just our jpanel).
        window.setLocationRelativeTo(null); // open window in the center of the screen
        window.setVisible(true); // display the window
        board.setCurves(new Curve[]{curve, curve1, curve2, curve3}); //setting the random curves
        board.repaint(); //repainting the board
    }
}