package ar.edu.ips.aus.seminario2.sampleproject;

public class Player {
    public static final double OFFSET_VELOCITY = 5.0;
    public static final double OFFSET_STEPS = 100.0;
    private int x;
    private int y;
    private double yOffset = 0.0;
    private double xOffset = 0.0;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getYOffset() {
        return this.yOffset;
    }

    public void setYOffset(double offset) {
        this.yOffset = offset;
    }

    public double getBoardX() {
        return this.xOffset + this.x;
    }

    public double getBoardY() {
        return this.yOffset + this.y;
    }

    public double getXOffset() {
        return this.xOffset;
    }

    public void setXOffset(double offset) {
        this.xOffset = offset;
    }
}
