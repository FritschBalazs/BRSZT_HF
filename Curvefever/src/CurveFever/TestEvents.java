package CurveFever;

import CurveFever.gui.GameScreen;
import CurveFever.gui.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TestEvents {
    public static void main(String[] args){
        JFrame window = new JFrame("Kurve Fívör gui");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ScreenManager screenManager = new ScreenManager();

        Curve curve = Main.generateRandomCurve(500,0, 720, Color.PINK, 70);
        Curve curve1 = Main.generateRandomCurve(500,0, 720, Color.BLUE, 70);
        Curve curve2 = Main.generateRandomCurve(500,0, 720, Color.RED, 70);
        Curve curve3 = Main.generateRandomCurve(500,0, 720, Color.GREEN, 70);


        Board board = new Board(4,5,new String[]{"Hanti","Dani", "Frici","valaki"},new Color[]{Color.PINK,Color.PINK,Color.PINK,Color.PINK});
        board.setCurves(new Curve[]{curve, curve1, curve2, curve3}); //setting the random curves
        //System.out.println(board.getPlayerNames()[0]);
        GameScreen gamescreen = new GameScreen(4);
        gamescreen.setCurves(board.getCurves());
        gamescreen.setCurrentRound(board.getCurrentRound());
        gamescreen.setPlayerNames(board.getPlayerNames());
        //System.out.println(gamescreen.getPlayerNames()[0]);
        gamescreen.setCurves(board.getCurves());
        gamescreen.setScores(board.getScores());
        //System.out.println(gamescreen.getInfoPanel().getPlayerNames()[0]);
        gamescreen.setNumOfPlayers(4);

        double[] points = new double[4];
        for (int i = 0; i < 4; i = i+1) {
            points[i] = i;
        }
        gamescreen.setScores(points);
        //gamescreen.render();
        //System.out.println(gamescreen.getInfoPanel().getPlayerNames()[0]);
        screenManager.setGameScreen(gamescreen);
        //System.out.println(screenManager.getGameScreen().getInfoPanel().getPlayerNames()[0]);




        window.add(screenManager);
        window.pack();
        window.setVisible(true);
        Timer timer = new Timer(100, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                screenManager.update(true);

            }
        });
        timer.start();
    }
}
