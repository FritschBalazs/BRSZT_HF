package CurveFever;

import java.awt.*;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.runMenu();
    }



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


}