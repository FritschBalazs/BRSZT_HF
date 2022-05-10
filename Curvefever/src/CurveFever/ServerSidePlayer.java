package CurveFever;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class ServerSidePlayer extends Player{

    public static final int SYSTEM_TICK = 10;  // In milliseconds
    public static final int TURN_DEGREE_PER_SECOND = 30;   // Turns 30 degrees in one second
    public static final double TURN_DEGREE_PER_TICK = TURN_DEGREE_PER_SECOND / SYSTEM_TICK;

    private Vector2D position;
    private Vector2D speed;
    private String ipAddress;
    private Color playerColor;
    private boolean isAlive;
    private int score;   /* TODO change score type to int*/

    public ServerSidePlayer(String name, String ipAddress, Color pColor, int pId, Vector2D startingPos) {
        super(name,pId);

        this.position = startingPos;
        this.speed.setCoordinates(0,0);
        this.ipAddress = ipAddress;
        this.playerColor = pColor;
        this.isAlive = true;
        this.score = 0;
    }

    private void rotateSpeed(double angle){
        double xTemp = this.speed.getX() * cos(toRadians(angle)) - this.speed.getY() * sin(toRadians(angle));
        double yTemp = this.speed.getX() * sin(toRadians(angle)) + this.speed.getY() * cos(toRadians(angle));
        speed.setCoordinates(xTemp, yTemp);
    }

    public void setPosition(Vector2D pos) {
        this.position = pos;
    }

    public void setSpeed(Vector2D speed) {
        this.speed = speed;
    }

    public void setIpAddress(String ipAddr) {
        this.ipAddress = ipAddr;
    }

    public void setPlayerColor(Color color) {
        this.playerColor = color;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getSpeed() {
        return speed;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public double getScore() {
        return score;
    }

    public void move(){
        double angle;   // in degrees
        if (this.controlState == ControlState.LEFT)
            angle = TURN_DEGREE_PER_TICK;
        else if (this.controlState == ControlState.RIGHT)
            angle = -1 * TURN_DEGREE_PER_TICK;
        else angle = 0;

        rotateSpeed(angle);
        this.position.setX(this.position.getX() + this.speed.getX());
        this.position.setY(this.position.getY() + this.speed.getY());
    }

    public void updateScore(double scoreToAdd){
        this.score += scoreToAdd;
    }
}
