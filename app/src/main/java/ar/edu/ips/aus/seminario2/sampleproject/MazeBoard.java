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
    private Player player = new Player(0.5,0.5);

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
        int pieceXPos = (int) player.getX();
        int pieceYPos = (int) player.getY();
        switch(dir) {
            case NORTH:
                if ((player.getY() > (double)pieceYPos + 0.5d) ||
                        getPiece(pieceXPos, pieceYPos).isOpen(Direction.NORTH)) {
                    player.move(Direction.NORTH);
                    moved = true;
                } else {
                    player.stopOnY((double)pieceYPos + 0.5d );
                }
                break;
            case SOUTH:
                if ((player.getY() < (double)pieceYPos + 0.5d) ||
                        getPiece(pieceXPos, pieceYPos).isOpen(Direction.SOUTH)) {
                    player.move(Direction.SOUTH);
                    moved = true;
                } else {
                    player.stopOnY((double)pieceYPos + 0.5d);
                }
                break;
            case WEST:
                if ((player.getX() > (double)pieceXPos + 0.5d) ||
                        getPiece(pieceXPos, pieceYPos).isOpen(Direction.WEST)) {
                    player.move(Direction.WEST);
                    moved = true;
                } else {
                    player.stopOnX((double)pieceXPos + 0.5d);
                }
                break;
            case EAST:
                if ( (player.getX() < (double)pieceXPos + 0.5d) ||
                        getPiece(pieceXPos, pieceYPos).isOpen(Direction.EAST)) {
                    player.move(Direction.EAST);
                    moved = true;
                } else {
                    player.stopOnX((double)pieceXPos + 0.5d);
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
