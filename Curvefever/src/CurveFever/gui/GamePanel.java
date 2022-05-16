package CurveFever.gui;

import CurveFever.Curve;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

//the part where the curves are drawn
public class GamePanel extends JPanel {
    //private static final int width = 1280;
    //private static final int height = 720;
    private Curve[] Curves;
    private int numOfCurves;

    public GamePanel() {
        Curves = new Curve[0];
        numOfCurves = 0;
        //setBorder(BorderFactory.createLineBorder(Color.magenta,2));
        //setPreferredSize(new Dimension(width,height));
    }

    public void setCurves(Curve[] curves) {Curves = curves.clone();}

    public void drawCurves(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        for (int i = 0; i < Curves.length; i = i + 1) {
            g2d.setColor(Curves[i].getColor());
            for (int j = 1; j < Curves[i].getPoints().size(); j = j+1) {
                if ((Curves[i].getPoints().get(j).getIsColored()) && (Curves[i].getPoints().get(j-1).getIsColored())) {
                    g2d.draw(new Line2D.Double(Curves[i].getPoints().get(j-1).getX(), Curves[i].getPoints().get(j-1).getY(), Curves[i].getPoints().get(j).getX(), Curves[i].getPoints().get(j).getY()));
                }
            }
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw our custom graphics.
        setBackground(Color.gray);
        drawCurves(g);
        g.setColor(Color.magenta);
        g.drawRect(0,0,1280,720);
        Toolkit.getDefaultToolkit().sync(); // this smooths out animations on some systems
    }
}
