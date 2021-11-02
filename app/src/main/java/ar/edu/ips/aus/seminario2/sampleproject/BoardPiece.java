package ar.edu.ips.aus.seminario2.sampleproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

class BoardPiece {

    private List<MazeBoard.Direction> openings = new ArrayList<>();

    public BoardPiece(boolean west, boolean north, boolean east, boolean south){
        if (west)
            openings.add(MazeBoard.Direction.WEST);
        if (north)
            openings.add(MazeBoard.Direction.NORTH);
        if (east)
            openings.add(MazeBoard.Direction.EAST);
        if (south)
            openings.add(MazeBoard.Direction.SOUTH);
    }

    public BoardPiece() {
    }

    public boolean isOpen(MazeBoard.Direction direction){
        return openings.contains(direction);
    }

    public List<MazeBoard.Direction> getOpenings() {
        return openings;
    }

    public void setOpenings(List<MazeBoard.Direction> openings) {
        this.openings = openings;
    }
}
