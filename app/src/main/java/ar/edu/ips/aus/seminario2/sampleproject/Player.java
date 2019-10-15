package ar.edu.ips.aus.seminario2.sampleproject;

public class Player {
    private double x;
    private double y;
    private double xVel = 0.0;
    private double yVel = 0.0;
    private static double VEL_FACTOR = 0.02;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getXVel() {
        return xVel;
    }

    public void setXVel(double xVel) {
        this.xVel = xVel;
    }

    public double getYVel() {
        return yVel;
    }

    public void setYVel(double yVel) {
        this.yVel = yVel;
    }

    public void move(MazeBoard.Direction direction) {
        switch (direction){
            case NORTH:
                this.yVel = -1.0 * VEL_FACTOR;
                this.xVel = 0.0;
                break;
            case SOUTH:
                this.yVel = 1.0 * VEL_FACTOR;
                this.xVel = 0.0;
                break;
            case EAST:
                this.yVel = 0.0;
                this.xVel = 1.0 * VEL_FACTOR;
                break;
            case WEST:
                this.yVel = 0.0;
                this.xVel = -1.0 * VEL_FACTOR;
                break;
            default:
                this.yVel = 0.0;
                this.xVel = 0.0;
                break;
        }
        this.computeMovement();
    }

    public void computeMovement() {
        this.x = this.x + this.xVel;
        this.y = this.y + this.yVel;
    }

    public void stopOnX(double x) {
        this.x = x;
        this.xVel = 0.0;
    }

    public void stopOnY(double y) {
        this.y = y;
        this.yVel = 0.0;
    }
}
