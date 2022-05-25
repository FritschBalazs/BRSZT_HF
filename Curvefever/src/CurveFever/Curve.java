package CurveFever;

import java.awt.*;
import java.util.ArrayList;

public class Curve {
    private ArrayList<CurvePoint> Points = new ArrayList<CurvePoint>();
    private Color color;

    public Curve(){}
    public Color getColor() {return color;}

    public ArrayList<CurvePoint> getPoints() {return new ArrayList<CurvePoint>(Points);}

    public CurvePoint getPoint(int idx) {
        return Points.get(idx);
    }

    public int getCurveSize() {
        return Points.size();
    }

    public CurvePoint getLastPoint() {
        return Points.get(Points.size()-1);
    }

    public void setColor(Color color) {this.color = color;}

    public void setPoints(ArrayList<CurvePoint> points) {Points = new ArrayList<CurvePoint>(points);}

    public void addPoint(CurvePoint curvePoint) {
        Points.add(curvePoint);
    }

    public void clearCurves(){
        Points.clear();
    }
}
