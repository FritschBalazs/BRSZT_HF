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

    public int getNumOfCurves() {
        return Points.size();
    }

    public CurvePoint getLastPoint() {
        //if (Points.size() != 0){
            return Points.get(Points.size() - 1);
        //}
        //else return new CurvePoint();
    }

    public CurvePoint getLastLastPoint() {
        if (Points.size() >= 2){
            return Points.get(Points.size() - 2);
        }
        else if (Points.size() >= 1){
            return Points.get(Points.size() - 1);
        }
        else{
            return null;
        }
    }

    public void setColor(Color color) {this.color = color;}

    public void setPoints(ArrayList<CurvePoint> points) {Points = new ArrayList<CurvePoint>(points);}

    public void addPoint(CurvePoint curvePoint) {
        Points.add(curvePoint);
    }

    public void setAPoint(int index, CurvePoint curvePoint){
        Points.set(index,curvePoint);
    }

    public void deleteLastPoint(){
        if (Points.size() >= 1) {
            Points.remove(Points.size() - 1);
        }
    }

    public void clearCurves(){
        Points.clear();
    }
}
