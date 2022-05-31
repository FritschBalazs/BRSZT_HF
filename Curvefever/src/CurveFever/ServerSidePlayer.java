package CurveFever;

import java.awt.*;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class ServerSidePlayer extends Player{

    public static final int SYSTEM_TICK = 20;  // In milliseconds
    public static final int TURN_DEGREE_PER_SECOND = 30;   // Turns 30 degrees in one second
    public static final int PREP_TIME = 200;   // IN SYSTEM ticks
    public static final double TURN_DEGREE_PER_TICK = TURN_DEGREE_PER_SECOND / SYSTEM_TICK;

    private Vector2D position;
    private Vector2D speed;
    private Color playerColor;
    private boolean isAlive;
    private double score;

    public ServerSidePlayer(String name, int pId) {
        super(name,pId);

        this.position = new Vector2D(0,0);
        this.speed = new Vector2D(0,0);
        this.playerColor = Color.RED;
        this.isAlive = true;
        this.score = 0;
    }

    public void setPosition(Vector2D pos) {
        this.position = pos;
    }

    public void setSpeed(Vector2D speed) {
        this.speed = speed;
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

    public Color getPlayerColor() {
        return playerColor;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public double getScore() {
        return score;
    }

    public void rotateSpeed(double angle){
        double xTemp = this.speed.getX() * cos(toRadians(angle)) - this.speed.getY() * sin(toRadians(angle));
        double yTemp = this.speed.getX() * sin(toRadians(angle)) + this.speed.getY() * cos(toRadians(angle));
        speed.setCoordinates(xTemp, yTemp);
    }

    public void move(){
        double angle;   // in degrees
        if (this.controlState == ControlState.LEFT)
            angle = -1 * TURN_DEGREE_PER_TICK;
        else if (this.controlState == ControlState.RIGHT)
            angle = TURN_DEGREE_PER_TICK;
        else angle = 0;

        rotateSpeed(angle);
        this.position.setX(this.position.getX() + this.speed.getX());
        this.position.setY(this.position.getY() + this.speed.getY());
    }

    public void updateScore(double scoreToAdd){
        this.score += scoreToAdd;
    }
}
