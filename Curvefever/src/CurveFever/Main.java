package CurveFever;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {

    /* this is a file to test baoard and menu objects */
    /* created by Dani */

    private static final Color BLACK = new Color(0,0,0);
    private static final Color PINK = new Color(255,105,180);
    private static final Color BLUE = new Color(0,255,255);
    private static final Color RED = new Color(255,0,0);
    private static final Color GREEN = new Color(0,255,0);
    public static Curve generateRandomCurve(int numOfPoints, int rangeMin, int rangeMax, Color color, float percentOfIsColored){
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
            CurvePoint randomCurvePoint = new CurvePoint(randomx, randomy, isColoredWProbability);
            curve.addPoint(randomCurvePoint);
        }
        return curve;
    }

    public static void main(String[] args) {
        //generating some random curves
        Curve curve = generateRandomCurve(500,0, 720, PINK, 70);
        Curve curve1 = generateRandomCurve(500,0, 720, BLUE, 70);
        Curve curve2 = generateRandomCurve(500,0, 720, RED, 70);
        Curve curve3 = generateRandomCurve(500,0, 720, GREEN, 70);


        JFrame window = new JFrame("Kurve Fívör");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // when we close the window, stop the app
        Board board = new Board(4,5,new String[]{"Hanti","Dani", "Frici","valaki"},new Color[]{PINK,PINK,PINK,PINK});
        board.addInfoPanel();
        //window.setSize(new Dimension(2000,2000));
        //window.add(board); // add the jpanel to the window
        /*JPanel probapanel = new JPanel();
        probapanel.setBackground(Color.red);
        probapanel.setPreferredSize(new Dimension(100,720));
        probapanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        probapanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.X_AXIS));*/
        window.add(board);
        //window.add(probapanel);

        window.setResizable(false); // don't allow the user to resize the window
        ImageIcon logo = new ImageIcon("src/curvefeverlogo.jpg");
        window.setIconImage(logo.getImage()); //adding logo to window
        window.pack(); // fit the window size around the components (just our jpanel).

        window.setLocationRelativeTo(null); // open window in the center of the screen
        board.setCurves(new Curve[]{curve, curve1, curve2, curve3}); //setting the random curves
        //board.render(); //repainting the board //commented out to test the gui
        window.setVisible(true); // display the window
    }
}