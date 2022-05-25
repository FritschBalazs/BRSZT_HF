package CurveFever.gui;

import CurveFever.Curve;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

//the part where the curves are drawn
public class GamePanel extends JPanel {
    //private static final int width = 1280;
    //private static final int height = 720;
    private Curve[] Curves;
    private int numOfCurves;
    private BufferedImage boardImage;

    public GamePanel() {
        Curves = new Curve[0];
        numOfCurves = 0;
        //setBorder(BorderFactory.createLineBorder(Color.magenta,2));
        //setPreferredSize(new Dimension(width,height));
        boardImage = new BufferedImage(1220,700,BufferedImage.TYPE_INT_RGB);
    }

    public void setCurves(Curve[] curves) {Curves = curves.clone();}

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
    @Override
    public void paintComponent(Graphics g) {
        long startTime = System.nanoTime();
        super.paintComponent(g);
        // draw our custom graphics.
        setBackground(Color.gray);
        //drawCurves(g); //old method

        updateBoardImage();
        g.drawImage(boardImage,0, 0,this);

        g.setColor(Color.magenta);
        g.drawRect(0,0,1280,720);


        Toolkit.getDefaultToolkit().sync(); // this smooths out animations on some systems

        long endTime = System.nanoTime();
        System.out.println("Paint time: "+ (endTime-startTime)/1000000);
    }
}
