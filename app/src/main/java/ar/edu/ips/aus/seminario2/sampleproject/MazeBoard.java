package ar.edu.ips.aus.seminario2.sampleproject;


import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * A maze 2D rectangular board. Knows the maze layout, dimensions. Can be queried for width, height
 * and piece by positional (0 based index). Can export/import textual representation.
 */
public class MazeBoard {

    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    private int width = 0;
    private int height = 0;
    private BoardPiece[] board = null;
    private Player player = new Player(0,0);

    private Direction playerDirection = Direction.NORTH;

    // FIXME more appropiate to call tile count?
    public int getHeight() {return height;}

    // FIXME more appropiate to call tile count?
    public int getWidth() { return width;}

    public BoardPiece getPiece(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new ArrayIndexOutOfBoundsException("Check your coordinates!");
        }
        return board[x%width+height*y];
    }

    private MazeBoard() {}

    public static MazeBoard from(String repr) {
        if (repr == null || repr.isEmpty())
            throw new IllegalArgumentException("Empty representation.");

        MazeBoard maze = new MazeBoard();

        // test with simple layout
        maze.board = new BoardPiece[9];
        maze.height = 3; maze.width = 3;
        maze.board[0] = new BoardPiece(false,false,true,true);
        maze.board[1] = new BoardPiece(true,false, true, false);
        maze.board[2] = new BoardPiece(true,false,false,true);
        maze.board[3] = new BoardPiece(false,true,false,true);
        maze.board[4] = new BoardPiece(true,true,true,true);
        maze.board[5] = new BoardPiece(false,true,false,true);
        maze.board[6] = new BoardPiece(false,true,true,false);
        maze.board[7] = new BoardPiece(true,false,true,false);
        maze.board[8] = new BoardPiece(true,true,false,false);

        return maze;
    }
    public String toString() {return null;}

    // retrieve single player
    public Player getPlayer() {
        return this.player;
    }

    // move single player
    private boolean movePlayer(Direction dir) {
        boolean moved = false;
        switch(dir) {
            case NORTH:
                if (player.getY() > 0 &&
                        (getPiece(player.getX(), player.getY()).isOpen(Direction.NORTH) &
                         getPiece(player.getX(), player.getY()-1).isOpen(Direction.SOUTH))) {
                    double newOffset = player.getYOffset() - Player.OFFSET_VELOCITY / Player.OFFSET_STEPS;
                    if (1.0 > newOffset & newOffset >  -1.0) {
                        player.setYOffset(newOffset);
                    } else {
                        player.setYOffset(0.0);
                        player.setY(player.getY()-1);
                    }
                    moved = true;
                }
                break;
            case SOUTH:
                if (player.getY() < getHeight()-1 &&
                        (getPiece(player.getX(), player.getY()).isOpen(Direction.SOUTH) &
                                getPiece(player.getX(), player.getY()+1).isOpen(Direction.NORTH))) {
                    double newOffset = player.getYOffset() + Player.OFFSET_VELOCITY / Player.OFFSET_STEPS;
                    if (-1.0 < newOffset & newOffset <  1.0) {
                        player.setYOffset(newOffset);
                    } else {
                        player.setYOffset(0.0);
                        player.setY(player.getY()+1);
                    }
                    moved = true;
                }
                break;
            case WEST:
                if (player.getX() > 0 &&
                        (getPiece(player.getX(), player.getY()).isOpen(Direction.WEST) &
                                getPiece(player.getX()-1, player.getY()).isOpen(Direction.EAST))) {
                    double newOffset = player.getXOffset() - Player.OFFSET_VELOCITY / Player.OFFSET_STEPS;
                    if (1.0 > newOffset & newOffset >  -1.0) {
                        player.setXOffset(newOffset);
                    } else {
                        player.setXOffset(0.0);
                        player.setX(player.getX()-1);
                    }
                    moved = true;
                }
                break;
            case EAST:
                if (player.getX() < getWidth()-1 &&
                        (getPiece(player.getX(), player.getY()).isOpen(Direction.EAST) &
                                getPiece(player.getX()+1, player.getY()).isOpen(Direction.WEST))) {
                    double newOffset = player.getXOffset() + Player.OFFSET_VELOCITY / Player.OFFSET_STEPS;
                    if (1.0 > newOffset & newOffset >  -1.0) {
                        player.setXOffset(newOffset);
                    } else {
                        player.setXOffset(0.0);
                        player.setX(player.getX()+1);
                    }
                    moved = true;
                }
                break;
        }
        return moved;
    }

    public void setNewDirection(Direction direction) {
        this.playerDirection = direction;
    }


    public Direction getPlayerDirection() {
        return playerDirection;
    }

    public void update() {
        movePlayer(playerDirection);
    }

}
