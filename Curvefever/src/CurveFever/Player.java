package CurveFever;

public class Player {
    protected int id;
    protected ControlState controlState;
    protected String name;
    // note, ide nem kell color, mert sajat maganak folosleges tudni, a tablan ugyis rajta lesz hogy milyet rajzoljon
    // ki

    public Player(String name) {
        this.name = name;
        id = -1;    //-1 -> invalid. Id will be given later by the server
        controlState = ControlState.STRAIGHT;
    }

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        controlState = ControlState.STRAIGHT;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setControlState(ControlState cState) {
        this.controlState = cState;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public ControlState getControlState() {
        return controlState;
    }

    public String getName() {
        return name;
    }

    public void getInput() {

    }

}
