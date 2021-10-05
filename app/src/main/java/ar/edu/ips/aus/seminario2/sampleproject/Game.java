package ar.edu.ips.aus.seminario2.sampleproject;

import android.content.Context;
import android.provider.Settings;

import java.util.Random;
import java.util.Vector;

public class Game {

    private static Game app;
    private static Context context;

    public static Game getInstance() {
        if (app == null) {
            app = new Game();
        }
        return app;
    }

    public static Game getInstance(Context globalContext) {
        if (context == null) {
            context = globalContext;
        }
        return getInstance();
    }

    private MazeBoard mazeBoard;
    private String serverName;
    private boolean isGameServer;
    private Player player;
    private Vector<Player> players = new Vector<>();

    private Game() {}

    public MazeBoard getMazeBoard() {
        return mazeBoard;
    }

    public void setMazeBoard(MazeBoard mazeBoard) {
        this.mazeBoard = mazeBoard;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public boolean isGameServer() {
        return isGameServer;
    }

    public void setGameServer(boolean gameServer) {
        isGameServer = gameServer;
    }

    public void initPlayers() {
        String id = Settings.Secure.getString(this.context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        player = new Player(id,0.5,0.5);
        Player player2 = new Player("NNNN", 8.5, 8.5);
        player2.setNewDirection(MazeBoard.Direction.NORTH);
        Player player3 = new Player("ZZZZ", 0, 8.5);
        player3.setNewDirection(MazeBoard.Direction.NORTH);

        players.add(player);
        players.add(player2);
        players.add(player3);
    }

    public Player getPlayer() {
        return player;
    }

    public Vector<Player> getPlayers() {
        return players;
    }

    public void update() {
        // TODO update all players
        MazeBoard board = Game.getInstance().getMazeBoard();
        Random rand = new Random();
        MazeBoard.Direction[] values = MazeBoard.Direction.values();
        for (Player p: Game.getInstance().getPlayers()) {
            // randomly update other players
            if (p != Game.getInstance().getPlayer() &&
                    p.getDirection() == MazeBoard.Direction.NONE){
                p.setNewDirection(values[rand.nextInt(values.length)]);
            }
            p.move(board);
        }
/*
        // TODO send coordinates
        if (client != null){
            MessageWrapper message = new MessageWrapper();
            message.setMessage(String.format("position: %f2.2,%f2.2", this.board.getPlayer().getX(), this.board.getPlayer().getY()));
            message.setMessageType(MessageWrapper.MessageType.NORMAL);
            client.sendMessageToServer(message);
        }

        Log.d("MOVE:", String.format("position: %2.2f,%2.2f", this.board.getPlayer().getX(), this.board.getPlayer().getY()));
*/
    }

}
