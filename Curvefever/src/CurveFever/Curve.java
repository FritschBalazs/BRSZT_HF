package CurveFever;

import java.util.ArrayList;

public class Curve {
    private ArrayList<CurvePoint> Points;   //https://www.w3schools.com/java/java_arraylist.asp
    //TODO ezt atgondolni, hogy nem jobb-e implements-el, vagy egyszeruen kihagyni
    // az egeszet, es a boardban listat tarolni, vagy eseleg protected?

    public Curve() {
        Points = new ArrayList<CurvePoint>();
    }


}
