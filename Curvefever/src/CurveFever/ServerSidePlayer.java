package CurveFever;

public class ServerSidePlayer extends Player{

    private Vector2D position;
    private Vector2D speed;
    private String ipAddress; //TODO delete ipAddress
    private Color playerColor;
    private boolean isAlive;
    private double score;

    public ServerSidePlayer(String name, Color pColor, int pId, Vector2D startingPos) {
        super(name,pId);

        this.position = startingPos;
        this.speed.setCoordinates(0,0);
        this.ipAddress = ipAddress;
        this.playerColor = pColor;
        this.isAlive = true;
        this.score = 0;
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

    public void setScore(double score) {
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

    public  Vector2D move(){
        return null;
    }

}
