package ar.edu.ips.aus.seminario2.sampleproject;

public class GameMetadata {

    public enum GameStatus {
        NEW,
        RUNNING,
        PAUSED,
        FINISHED
    }

    private String id;
    private String title;
    private MazeBoard gameBoard;
    public GameStatus status = GameStatus.NEW;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        try {
            this.status = GameStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            // intentionally left-blank
        }
    }
}
