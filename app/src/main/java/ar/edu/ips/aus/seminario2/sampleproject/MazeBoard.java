package ar.edu.ips.aus.seminario2.sampleproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A maze 2D rectangular board. Knows the maze layout, dimensions. Can be queried for width, height
 * and piece by positional (0 based index). Can export/import textual representation.
 */
public class MazeBoard {

    public enum Direction {
        NONE,
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    private int width = 0;
    private int height = 0;
    private List<BoardPiece> board = null;

    public int getVerticalTileCount() {return height;}

    public void setVerticalTileCount(int height){
        this.height = height;
    }

    public int getHorizontalTileCount() { return width;}

    public void setHorizontalTileCount(int width){
        this.width = width;
    }

    public List<BoardPiece> getBoard() {
        return board;
    }

    public void setBoard(List<BoardPiece> board) {
        this.board = board;
    }

    public BoardPiece getPiece(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new ArrayIndexOutOfBoundsException("Check your coordinates!");
        }
        return board.get(x%width+height*y);
    }

    private MazeBoard() {}

    public static MazeBoard from(String repr) {
        if (repr == null || repr.isEmpty())
            throw new IllegalArgumentException("Empty representation.");

        MazeBoard maze = new MazeBoard();

        // test with simple layout
        maze.height = 9; maze.width = 9;
        BoardPiece[] simpleBoard = new BoardPiece[81];
        simpleBoard[0] = new BoardPiece(false,false,false,true);
        simpleBoard[1] = new BoardPiece(false,false, true, true);
        simpleBoard[2] = new BoardPiece(true,false,true,false);
        simpleBoard[3] = new BoardPiece(true,false,true,false);
        simpleBoard[4] = new BoardPiece(true,false,true,false);
        simpleBoard[5] = new BoardPiece(true,false,false,true);
        simpleBoard[6] = new BoardPiece(false,false,true,true);
        simpleBoard[7] = new BoardPiece(true,false,true,false);
        simpleBoard[8] = new BoardPiece(true,false,false, true);

        simpleBoard[9] = new BoardPiece(false,true,true,false);
        simpleBoard[10] = new BoardPiece(true,true,false,false);
        simpleBoard[11] = new BoardPiece(false,false, true, false);
        simpleBoard[12] = new BoardPiece(true,false,true,false);
        simpleBoard[13] = new BoardPiece(true,false,true,true);
        simpleBoard[14] = new BoardPiece(true,true,false,true);
        simpleBoard[15] = new BoardPiece(false,true,false,true);
        simpleBoard[16] = new BoardPiece(false,false,false,true);
        simpleBoard[17] = new BoardPiece(false,true,false,true);

        simpleBoard[18] = new BoardPiece(false,false,true,true);
        simpleBoard[19] = new BoardPiece(true,false,true,false);
        simpleBoard[20] = new BoardPiece(true,false, true, false);
        simpleBoard[21] = new BoardPiece(true,false,true,false);
        simpleBoard[22] = new BoardPiece(true,true,false,false);
        simpleBoard[23] = new BoardPiece(false,true,true,false);
        simpleBoard[24] = new BoardPiece(true,true,false,false);
        simpleBoard[25] = new BoardPiece(false,true,true,false);
        simpleBoard[26] = new BoardPiece(true,true,false,true);

        simpleBoard[27] = new BoardPiece(false,true,true,false);
        simpleBoard[28] = new BoardPiece(true,false,false,true);
        simpleBoard[29] = new BoardPiece(false,false,true,true);
        simpleBoard[30] = new BoardPiece(true,false, true, false);
        simpleBoard[31] = new BoardPiece(true,false,true,false);
        simpleBoard[32] = new BoardPiece(true,false,true,false);
        simpleBoard[33] = new BoardPiece(true,false,false,true);
        simpleBoard[34] = new BoardPiece(false,false,true,true);
        simpleBoard[35] = new BoardPiece(true,true,false,false);

        simpleBoard[36] = new BoardPiece(false,false,true,true);
        simpleBoard[37] = new BoardPiece(true,true,false,true);
        simpleBoard[38] = new BoardPiece(false,true,false,true);
        simpleBoard[39] = new BoardPiece(false,false,true,false);
        simpleBoard[40] = new BoardPiece(true,false, true, false);
        simpleBoard[41] = new BoardPiece(true,false,true,true);
        simpleBoard[42] = new BoardPiece(true,true,false,false);
        simpleBoard[43] = new BoardPiece(false,true,true,true);
        simpleBoard[44] = new BoardPiece(true,false,false,true);

        simpleBoard[45] = new BoardPiece(false,true,false,true);
        simpleBoard[46] = new BoardPiece(false,true,false,false);
        simpleBoard[47] = new BoardPiece(false,true,true,false);
        simpleBoard[48] = new BoardPiece(true,false,true,false);
        simpleBoard[49] = new BoardPiece(true,false,false,true);
        simpleBoard[50] = new BoardPiece(false,true, true, false);
        simpleBoard[51] = new BoardPiece(true,false,true,false);
        simpleBoard[52] = new BoardPiece(true,true,false,false);
        simpleBoard[53] = new BoardPiece(false,true,false,true);

        simpleBoard[54] = new BoardPiece(false,true,false,true);
        simpleBoard[55] = new BoardPiece(false,false,true,true);
        simpleBoard[56] = new BoardPiece(true,false,true,false);
        simpleBoard[57] = new BoardPiece(true,false,false,true);
        simpleBoard[58] = new BoardPiece(false,true,true,false);
        simpleBoard[59] = new BoardPiece(true,false,true,false);
        simpleBoard[60] = new BoardPiece(true,false, false, true);
        simpleBoard[61] = new BoardPiece(false,false,true,true);
        simpleBoard[62] = new BoardPiece(true,true,false,false);

        simpleBoard[63] = new BoardPiece(false,true,false,true);
        simpleBoard[64] = new BoardPiece(false,true,true,false);
        simpleBoard[65] = new BoardPiece(true,false,false,true);
        simpleBoard[66] = new BoardPiece(false,true,true,false);
        simpleBoard[67] = new BoardPiece(true,false,true,false);
        simpleBoard[68] = new BoardPiece(true,false,false,true);
        simpleBoard[69] = new BoardPiece(false,true,false,true);
        simpleBoard[70] = new BoardPiece(false,true, true, false);
        simpleBoard[71] = new BoardPiece(true,false,false,true);

        simpleBoard[72] = new BoardPiece(false,true,true,false);
        simpleBoard[73] = new BoardPiece(true,false,false,false);
        simpleBoard[74] = new BoardPiece(false,true,true,false);
        simpleBoard[75] = new BoardPiece(true,false,true,false);
        simpleBoard[76] = new BoardPiece(true,false,false,false);
        simpleBoard[77] = new BoardPiece(false,true,true,false);
        simpleBoard[78] = new BoardPiece(true,true,false,false);
        simpleBoard[79] = new BoardPiece(false,false,true,false);
        simpleBoard[80] = new BoardPiece(true,true, false, false);

        maze.board = Arrays.asList(simpleBoard);
        return maze;
    }

    public String toString() {return String.format("Mazeboard %dx%d", this.width, this.height);}

}
