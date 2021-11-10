package ar.edu.ips.aus.seminario2.sampleproject;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ar.edu.ips.aus.seminario2.sampleproject.GameMetadata.GameStatus.*;

public class Game {

    private static Game app;
    public static String ID;
    private Map<String, Player> players = new HashMap<>();
    private GameMetadata gameMetadata;

    private static Context context;
    private static DatabaseReference playerDatabase;
    private static final String TAG = "PLAYER";
    private static DatabaseReference statusDatabase;

    public static Game getInstance() {
        if (app == null) {
            app = new Game();
        }
        return app;
    }

    public static Game getInstance(Context globalContext) {
        if (context == null) {
            context = globalContext;
            ID = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        return getInstance();
    }

    private Game() {}

    public MazeBoard getMazeBoard() {
        return gameMetadata.getGameBoard();
    }

    public void setMazeBoard(MazeBoard mazeBoard) {
        this.gameMetadata.setGameBoard(mazeBoard);
    }

    public void initPlayers() {
        players.clear();
        Player player = new Player(ID,0.5,0.5);
        players.put(ID, player);

        initPlayerDatabase();
        initStatusDatabase();
    }

    private void initPlayerDatabase() {
        if (playerDatabase != null) {
            playerDatabase.removeEventListener(playerDataListener);
        }
        String path = String.format("/%s/players",gameMetadata.getId());
        playerDatabase = FirebaseDatabase.getInstance().getReference(path);
        playerDatabase.addValueEventListener(playerDataListener);
    }

    private void initStatusDatabase() {
        String path = String.format("/%s/status",gameMetadata.getId());
        statusDatabase = FirebaseDatabase.getInstance().getReference(path);
    }

    ValueEventListener playerDataListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                GenericTypeIndicator<HashMap<String, Player>> tweakingTypeIndicator =
                        new GenericTypeIndicator<HashMap<String, Player>>() {
                        };
                HashMap<String, Player> inboundPlayers = snapshot.getValue(tweakingTypeIndicator);
                for (Player player : inboundPlayers.values()) {
                    if (player.getID() != getInstance().ID) {
                        getInstance().players.put(player.getID(), player);
                    }
                    //Log.d(TAG, player.toString());
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w(TAG, "onCancelled", error.toException());
        }
    };

    public Player getPlayer() {
        return players.get(ID);
    }

    public Player getPlayerById(String id) {
        return players.get(ID);
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public void update() {
        // update only local player
        MazeBoard board = Game.getInstance().getMazeBoard();
        this.getPlayer().move(board);
        sendPlayerData();
   }

    private void sendPlayerData() {
        playerDatabase.child(ID).setValue(getPlayer());
    }

    public void setGameMetadata(GameMetadata metadata) {
        this.gameMetadata = metadata;
    }

    public GameMetadata getGameMetadata() {
        return this.gameMetadata;
    }

    public void setStatus(String status){
        this.gameMetadata.setStatus(status);
    }

    public GameMetadata.GameStatus getStatus() {
        return this.gameMetadata.getStatus();
    }

    public void pauseOrStart() {
        switch (gameMetadata.status) {
            case NEW:
            case PAUSED:
                gameMetadata.setStatus(RUNNING.name());
                updateGameStatus();
                break;
            case RUNNING:
                gameMetadata.setStatus(PAUSED.name());
                updateGameStatus();
                break;
            default:
                break;
        }
    }

    private void updateGameStatus() {
        statusDatabase.setValue(gameMetadata.getStatus());
    }
}
