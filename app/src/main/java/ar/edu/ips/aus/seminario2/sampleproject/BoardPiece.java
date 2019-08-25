package ar.edu.ips.aus.seminario2.sampleproject;

import java.util.Vector;

class BoardPiece {

    private final Vector<Direction> openings = new Vector<Direction>();

    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    public BoardPiece(boolean west, boolean north, boolean east, boolean south){
        if (west)
            openings.add(Direction.WEST);
        if (north)
            openings.add(Direction.NORTH);
        if (east)
            openings.add(Direction.EAST);
        if (south)
            openings.add(Direction.SOUTH);
    }

    // TODO delete public method
    private Direction[] getOpenings() { return (Direction[]) openings.toArray();}

    public boolean openTo(Direction direction){
        return openings.contains(direction);
    }

}
