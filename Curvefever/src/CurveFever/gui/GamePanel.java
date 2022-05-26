package CurveFever.gui;

import CurveFever.Curve;
import CurveFever.CurvePoint;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

//the part where the curves are drawn
public class GamePanel extends JPanel {
    private static final Color BUTTONCOLOR2 = new Color(3, 252, 217);
    private int boardWidth = 1280;
    private int boardHeight = 720;
    private Curve[] Curves; //majd lehet torolni es eleg lesz a points is
    private CurvePoint[] PrevCurvePoints;
    private CurvePoint[] CurvePoints;
    private Color[] Colors;
    private boolean initHappened = false;
    private int numOfPlayers;
    private BufferedImage boardImage;

    public GamePanel() {
        Curves = new Curve[0]; //lehet torolni majd
        numOfPlayers = 5;
        CurvePoints = new CurvePoint[numOfPlayers];
        PrevCurvePoints = new CurvePoint[numOfPlayers];
        /*for (int i = 0; i<numOfPlayers;i++){
            CurvePoints[i] = new CurvePoint();
            PrevCurvePoints[i] = new CurvePoint();
        }*/

    }

    public void setBoardImage(BufferedImage boardImage) {
        this.boardImage = boardImage;
    }

    public void setInitHappened(boolean initHappened) {
        this.initHappened = initHappened;
    }

    public void setCurves(Curve[] curves) {Curves = curves.clone();}

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    public void setCurvePoints(CurvePoint[] curvePoints) {
        CurvePoints = curvePoints.clone();
    }

    public void setColors(Color[] colors) {
        Colors = colors.clone();
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public void setPrevCurvePoints(CurvePoint[] prevCurvePoints) {
        PrevCurvePoints = prevCurvePoints.clone();
    }

    public CurvePoint[] getCurvePoints() {
        return CurvePoints.clone();
    }

    public CurvePoint[] getPrevCurvePoints() {
        return PrevCurvePoints.clone();
    }

    /*old method, can be deleted with last cleanup */ //TODO (B/D low prio) majd a vegen ezt kitakaritani
    public void drawCurves(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        for (Curve curve : Curves) {
            g2d.setColor(curve.getColor());
            for (int j = 1; j < curve.getPoints().size(); j = j + 1) {
                if ((curve.getPoints().get(j).getIsColored()) && (curve.getPoints().get(j - 1).getIsColored())) {
                    g2d.draw(new Line2D.Double(curve.getPoints().get(j - 1).getX(), curve.getPoints().get(j - 1).getY(), curve.getPoints().get(j).getX(), curve.getPoints().get(j).getY()));
                }
            }
        }
    }

    private void updateBoardImage() {

        Graphics2D g = (Graphics2D)boardImage.createGraphics();
        g.setStroke(new BasicStroke(3));

        for (int i = 0; i < Curves.length; i++) {
            g.setColor(Curves[i].getColor());
            int lastIndex= Curves[i].getPoints().size();
            if (lastIndex >= 2) {
                if (Curves[i].getPoints().get(lastIndex - 2).getIsColored() && Curves[i].getPoints().get(lastIndex - 1).getIsColored()) {
                    double x1 = Curves[i].getPoints().get(lastIndex - 2).getX();
                    double y1 = Curves[i].getPoints().get(lastIndex - 2).getY();
                    double x2 = Curves[i].getPoints().get(lastIndex - 1).getX();
                    double y2 = Curves[i].getPoints().get(lastIndex - 1).getY();
                    g.draw(new Line2D.Double(x1, y1, x2, y2));
                }
            }
        }
    }
    private void updateBoardImage2() {

        Graphics2D g = (Graphics2D)boardImage.createGraphics();
        g.setStroke(new BasicStroke(3));
        /*System.out.println((CurvePoints[0].getX()));
        System.out.println((PrevCurvePoints[0].getX()));*/
        for (int i = 0; i < numOfPlayers; i++) {
            if(initHappened) {
                g.setColor(Colors[i]);
                System.out.println(CurvePoints[0].getX());
                System.out.println(CurvePoints[0].getY());
                System.out.println(PrevCurvePoints[0].getX());
                System.out.println(PrevCurvePoints[0].getY());
                g.draw(new Line2D.Double(PrevCurvePoints[i].getX(), PrevCurvePoints[i].getY(), CurvePoints[i].getX(), CurvePoints[i].getY()));
            }
        }
        PrevCurvePoints = CurvePoints.clone();
        System.out.println(PrevCurvePoints[0].getX());
        System.out.println(PrevCurvePoints[0].getY());
    }
    @Override
    public void paintComponent(Graphics g) {
        long startTime = System.nanoTime();
        super.paintComponent(g);
        setBackground(BUTTONCOLOR2);
        //drawCurves(g); //old method
        //updateBoardImage(); TEST
        updateBoardImage2();
        g.drawImage(boardImage,(this.getSize().width/2)-(boardWidth/2), (this.getSize().height/2)-(boardHeight/2),this);

        Toolkit.getDefaultToolkit().sync(); // this smooths out animations on some systems

        long endTime = System.nanoTime();
        System.out.println("Paint time: "+ (endTime-startTime)/1000000);
    }
}
