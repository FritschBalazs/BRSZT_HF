package CurveFever.gui;

import CurveFever.Curve;
import CurveFever.CurvePoint;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

//the part where the curves are drawn
public class GamePanel extends JPanel {
    private static final Color BUTTONCOLOR2 = new Color(3, 252, 217);
    private int boardWidth;
    private int boardHeight;
    private Curve[] Curves; //majd lehet torolni es eleg lesz a points is
    private CurvePoint[] PrevPrevCurvePoints;
    private CurvePoint[] PrevCurvePoints;
    private CurvePoint[] CurvePoints;
    private Color[] Colors;
    private boolean initHappened = false;
    private int numOfPlayers;
    private BufferedImage boardImage;
    public Path2D.Double[] Paths;

    public GamePanel() {
        Paths = new Path2D.Double[2];
        Paths[0] = new Path2D.Double();
        Paths[0].moveTo(500,500);
        Paths[1] = new Path2D.Double();
        Paths[1].moveTo(300,300);
    }

    public void setBoardImage(BufferedImage boardImage) {
        this.boardImage = boardImage;
    }

    public void setInitHappened(boolean initHappened) {
        this.initHappened = initHappened;
    }

    public void setPaths(Path2D.Double[] paths) {
        Paths = paths.clone();
    }
    public void addToPaths(){
        for(int i = 0; i<numOfPlayers; i++){
            //Paths[i].lineTo(CurvePoints[i].getX(),CurvePoints[i].getY());
            //Paths[i].quadTo(PrevCurvePoints[i].getX(),PrevCurvePoints[i].getY(),CurvePoints[i].getX(),CurvePoints[i].getY());
            Paths[i].curveTo(PrevPrevCurvePoints[i].getX(), PrevPrevCurvePoints[i].getY(),PrevCurvePoints[i].getX(), PrevCurvePoints[i].getY(),CurvePoints[i].getX(),CurvePoints[i].getY());
        }
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

    public void setPrevPrevCurvePoints(CurvePoint[] prevPrevCurvePoints) {
        PrevPrevCurvePoints = prevPrevCurvePoints;
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
    public void drawCurves2(Graphics g) { //old method With paths (redraws everything)
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        addToPaths();
        for (int i = 0; i < numOfPlayers; i++) {
            g2d.setColor(Colors[i]);
            g2d.draw(Paths[i]);
        }
        PrevPrevCurvePoints = PrevCurvePoints.clone();
        PrevCurvePoints = CurvePoints.clone();
    }

    private void updateBoardImage() {

        Graphics2D g = (Graphics2D)boardImage.createGraphics();
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(2));

        for (int i = 0; i < numOfPlayers; i++) {
                g.setColor(Colors[i]);
                if(CurvePoints[i].getIsColored()) {
                    g.draw(new Line2D.Double(PrevCurvePoints[i].getX(), PrevCurvePoints[i].getY(), CurvePoints[i].getX(), CurvePoints[i].getY()));
                }
        }
        PrevCurvePoints = CurvePoints.clone();
    }
    private void updateBoardImage2() { //with paths+buffered image

        Graphics2D g = (Graphics2D)boardImage.createGraphics();
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(2));

        for (int i = 0; i < numOfPlayers; i++) {
            g.setColor(Colors[i]);
            if(CurvePoints[i].getIsColored()) {
                //Paths[i].moveTo(PrevPrevCurvePoints[i].getX(), PrevPrevCurvePoints[i].getY());
                Paths[i].lineTo(CurvePoints[i].getX(),CurvePoints[i].getY());
                //Paths[i].quadTo(PrevCurvePoints[i].getX(), PrevCurvePoints[i].getY(),CurvePoints[i].getX(),CurvePoints[i].getY());
                //Paths[i].curveTo(PrevPrevCurvePoints[i].getX(), PrevPrevCurvePoints[i].getY(),PrevCurvePoints[i].getX(), PrevCurvePoints[i].getY(),CurvePoints[i].getX(),CurvePoints[i].getY());
                g.draw(Paths[i]);
            }
        }
        //PrevPrevCurvePoints = PrevCurvePoints.clone();
        //PrevCurvePoints = CurvePoints.clone();
    }

    public void resetBufferedImage()
    {
        /*when starting a new game create a clean Panel */
        Graphics2D graphics = boardImage.createGraphics();
        graphics.setPaint(new Color(26, 72, 98));  //background
        graphics.fillRect(0, 0, boardImage.getWidth(), boardImage.getHeight());
        graphics.setColor(Color.magenta);
        graphics.setStroke(new BasicStroke(3));
        graphics.drawRect(0,0,boardImage.getWidth()-1, boardImage.getHeight()-1);
    }

    @Override
    public void paintComponent(Graphics g) {
        long startTime = System.nanoTime();
        super.paintComponent(g);
        setBackground(BUTTONCOLOR2);
        updateBoardImage();
        //updateBoardImage2();
        g.drawImage(boardImage,(this.getSize().width/2)-(boardWidth/2), (this.getSize().height/2)-(boardHeight/2),this);
        //drawCurves2(g);


        Toolkit.getDefaultToolkit().sync(); // this smooths out animations on some systems

        long endTime = System.nanoTime();
        System.out.println("Paint time: "+ (endTime-startTime)/1000000);
    }
}
