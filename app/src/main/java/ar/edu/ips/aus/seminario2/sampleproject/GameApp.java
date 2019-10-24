package ar.edu.ips.aus.seminario2.sampleproject;

import com.abemart.wroup.client.WroupClient;
import com.abemart.wroup.service.WroupService;

public class GameApp {

    private static GameApp app;

    public static GameApp getInstance() {
        if (app == null) {
            app = new GameApp();
        }
        return app;
    }

    private MazeBoard mazeBoard;
    private String serverName;
    private boolean isGameServer;
    private WroupService server;
    private WroupClient client;

    private GameApp() {}

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

    public WroupService getServer() {
        return server;
    }

    public void setServer(WroupService server) {
        this.server = server;
    }

    public WroupClient getClient() {
        return client;
    }

    public void setClient(WroupClient client) {
        this.client = client;
    }
}
