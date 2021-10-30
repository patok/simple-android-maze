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

public class Game {

    private static Game app;
    private static Context context;
    private static DatabaseReference database;
    private static final String TAG = "PLAYER";

    private GameMetadata gameMetadata;

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
    private Map<String, Player> players = new HashMap<>();
    private String ID;

    private Game() {}

    public MazeBoard getMazeBoard() {
        return mazeBoard;
    }

    public void setMazeBoard(MazeBoard mazeBoard) {
        this.mazeBoard = mazeBoard;
    }

    public void initPlayers() {
        ID = Settings.Secure.getString(this.context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Player player = new Player(ID,0.5,0.5);
        players.put(ID, player);

        initDatabase();
    }

    private void initDatabase() {
        String path = String.format("/%s/players",gameMetadata.getId());
        database = FirebaseDatabase.getInstance().getReference(path);
        database.addValueEventListener(new ValueEventListener() {
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
                        Log.d(TAG, player.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled", error.toException());
            }
        });
    }

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
        // update all players move
        MazeBoard board = Game.getInstance().getMazeBoard();
        for (Player p: Game.getInstance().getPlayers()) {
            p.move(board);
        }
        sendPlayerData();
    }

    private void sendPlayerData() {
        database.child(ID).setValue(getPlayer());
    }

    public void setGameMetadata(GameMetadata metadata) {
        this.gameMetadata = metadata;
    }
}
