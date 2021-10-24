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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class Game {

    private static Game app;
    private static Context context;
    private static DatabaseReference database;
    private static final String TAG = "PLAYER";

    public static Game getInstance() {
        if (app == null) {
            app = new Game();
            database = FirebaseDatabase.getInstance().getReference("/players");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    GenericTypeIndicator<HashMap<String, Player>> tweakingTypeIndicator =
                            new GenericTypeIndicator<HashMap<String, Player>>() {};
                    HashMap<String, Player> players = snapshot.getValue(tweakingTypeIndicator);
                    for (Player player: players.values()) {
                        // FIXME improve player lookup and update!
                        for (Player localPlayer :
                                getInstance().getPlayers()) {
                            if (player.getID() == localPlayer.getID()) {
                                localPlayer.setX(player.getX());
                                localPlayer.setY(player.getY());
                                localPlayer.setXVel(player.getXVel());
                                localPlayer.setYVel(player.getYVel());
                            }
                        }
                        Log.d(TAG, player.toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "onCancelled", error.toException());
                }
            });
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
    private Player player;
    private Vector<Player> players = new Vector<>();
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
        player = new Player(ID,0.5,0.5);
        players.add(player);
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
            if (p.getID() != ID &&
                    p.getDirection() == MazeBoard.Direction.NONE){
                p.setNewDirection(values[rand.nextInt(values.length)]);
            }
            p.move(board);
        }
        sendPlayerData();
    }

    private void sendPlayerData() {
        database.child(player.getID()).setValue(player);
    }

}
