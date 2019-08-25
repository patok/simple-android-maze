package ar.edu.ips.aus.seminario2.sampleproject;


/**
 * A maze 2D rectangular board. Knows the maze layout, dimensions. Can be queried for width, height
 * and piece by positional (0 based index). Can export/import textual representation.
 */
public class MazeBoard {

    private int width = 0;
    private int height = 0;
    private BoardPiece[] board = null;

    public int getHeight() {return height;}

    public int getWidth() { return width;}

    public BoardPiece getPiece(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new ArrayIndexOutOfBoundsException("Check your coordinates!");
        }
        // test with simple layout
        return board[3*(y%3)+x];
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
        maze.board[1] = new BoardPiece(true,false, true, true);
        maze.board[2] = new BoardPiece(true,false,false,true);
        maze.board[3] = new BoardPiece(false,true,true,true);
        maze.board[4] = new BoardPiece(true,true,true,true);
        maze.board[5] = new BoardPiece(true,true,false,true);
        maze.board[6] = new BoardPiece(false,true,true,false);
        maze.board[7] = new BoardPiece(true,true,true,false);
        maze.board[8] = new BoardPiece(true,true,false,false);

        return maze;
    }
    public String toString() {return null;}
}
