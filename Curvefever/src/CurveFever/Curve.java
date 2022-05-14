package CurveFever;

import java.awt.*;
import java.util.ArrayList;

public class Curve {
    private ArrayList<CurvePoint> Points = new ArrayList<CurvePoint>();
    private Color color;

    public Curve(){}
    public Color getColor() {return color;}

    public ArrayList<CurvePoint> getPoints() {return new ArrayList<CurvePoint>(Points);}

    public void setColor(Color color) {this.color = color;}

    public void setPoints(ArrayList<CurvePoint> points) {Points = new ArrayList<CurvePoint>(points);}

    public void addPoint(CurvePoint curvePoint) {
        Points.add(curvePoint);
    }
}
