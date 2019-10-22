package ar.edu.ips.aus.seminario2.sampleproject;

import static ar.edu.ips.aus.seminario2.sampleproject.MazeBoard.Direction.EAST;
import static ar.edu.ips.aus.seminario2.sampleproject.MazeBoard.Direction.NONE;
import static ar.edu.ips.aus.seminario2.sampleproject.MazeBoard.Direction.NORTH;
import static ar.edu.ips.aus.seminario2.sampleproject.MazeBoard.Direction.SOUTH;
import static ar.edu.ips.aus.seminario2.sampleproject.MazeBoard.Direction.WEST;

public class Player {
    private final String ID;
    private double x;
    private double y;
    private double xVel = 0.0;
    private double yVel = 0.0;
    private final static double VEL_FACTOR = 0.04;

    public Player(String id, double x, double y) {
        this.ID = id;
        this.x = x;
        this.y = y;
    }

    public String getID() {
        return ID;
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

    public boolean move(MazeBoard board) {
        boolean moved = false;
        int pieceXPos = (int) getX();
        int pieceYPos = (int) getY();

        MazeBoard.Direction dir = NONE;
        dir = getDirection();

        switch(dir) {
            case NORTH:
                if ((getY() > (double)pieceYPos + 0.5d) ||
                        board.getPiece(pieceXPos, pieceYPos).isOpen(NORTH)) {
                    this.computeMovement();
                    moved = true;
                } else {
                    stopOnY((double)pieceYPos + 0.5d );
                }
                break;
            case SOUTH:
                if ((getY() < (double)pieceYPos + 0.5d) ||
                        board.getPiece(pieceXPos, pieceYPos).isOpen(MazeBoard.Direction.SOUTH)) {
                    this.computeMovement();
                    moved = true;
                } else {
                    stopOnY((double)pieceYPos + 0.5d);
                }
                break;
            case WEST:
                if ((getX() > (double)pieceXPos + 0.5d) ||
                        board.getPiece(pieceXPos, pieceYPos).isOpen(WEST)) {
                    this.computeMovement();
                    moved = true;
                } else {
                    stopOnX((double)pieceXPos + 0.5d);
                }
                break;
            case EAST:
                if ( (getX() < (double)pieceXPos + 0.5d) ||
                        board.getPiece(pieceXPos, pieceYPos).isOpen(EAST)) {
                    this.computeMovement();
                    moved = true;
                } else {
                    stopOnX((double)pieceXPos + 0.5d);
                }
                break;
        }
        return moved;
    }

    public MazeBoard.Direction getDirection() {
        if (this.xVel > 0.0)
            return EAST;
        else if (this.xVel < 0.0)
            return WEST;
        else if(this.yVel > 0.0)
            return SOUTH;
        else if(this.yVel < 0.0)
            return NORTH;
        return NONE;
    }

    private void computeMovement() {
        this.x = this.x + this.xVel;
        this.y = this.y + this.yVel;
    }

    private void stopOnX(double x) {
        this.x = x;
        this.xVel = 0.0;
    }

    private void stopOnY(double y) {
        this.y = y;
        this.yVel = 0.0;
    }

    public void setNewDirection(MazeBoard.Direction direction) {
        switch (direction){
            case NORTH:
                yVel = -1.0 * VEL_FACTOR;
                xVel = 0.0;
                break;
            case SOUTH:
                yVel = 1.0 * VEL_FACTOR;
                xVel = 0.0;
                break;
            case EAST:
                xVel = 1.0 * VEL_FACTOR;
                yVel = 0.0;
                break;
            case WEST:
                xVel = -1.0 * VEL_FACTOR;
                yVel = 0.0;
                break;
            default:
                xVel = 0.0;
                yVel = 0.0;
                break;
        }
    }
}
