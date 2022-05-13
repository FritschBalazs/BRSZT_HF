package CurveFever;

public class CurvePoint extends Vector2D{
    private boolean isColored;



    public CurvePoint(boolean isColored) {
        this.isColored = isColored;
    }
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

