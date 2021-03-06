package CurveFever;

import java.io.Serializable;

public class Vector2D implements Serializable {
    protected double x,y;

    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setCoordinates(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double[] getCoordinates() {
        return new double[]{x,y};
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
