package ar.edu.ips.aus.seminario2.sampleproject;

public class GameMetadata {

    private String id;
    private String title;
    private MazeBoard gameBoard;

    public GameMetadata() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MazeBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(MazeBoard gameBoard) {
        this.gameBoard = gameBoard;
    }
}
