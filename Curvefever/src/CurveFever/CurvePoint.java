package CurveFever;

public class CurvePoint extends Vector2D implements java.io.Serializable{
    private boolean isColored;

    public CurvePoint(boolean isColored) {
        this.isColored = isColored;
    }
    public CurvePoint(){}
    public CurvePoint(double x, double y, boolean isColored) {
        super(x,y);
        this.isColored = isColored;
    }

    public void  setIsColored(boolean isColored){
        this.isColored = isColored;
    }



    public boolean getIsColored() {
        return isColored;
    }

}

