package ar.edu.ips.aus.seminario2.sampleproject;

public class GameMetadata {

    private String id;
    private String title;
    private MazeBoard gameBoard;
    private String status;
    private String author;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
