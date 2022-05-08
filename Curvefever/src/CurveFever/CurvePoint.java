package CurveFever;

public class CurvePoint extends Vector2D{
    private boolean isColored;
    private Color color;


    public CurvePoint(boolean isColored, Color color) {
        this.isColored = isColored;
        this.color = color;
    }

    public void  setIsColored(boolean isColored){
        this.isColored = isColored;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public boolean getIsColored() {
        return isColored;
    }

    public Color getColor() {
        return color;
    }
}

