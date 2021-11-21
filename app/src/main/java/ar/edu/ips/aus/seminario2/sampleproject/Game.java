package ar.edu.ips.aus.seminario2.sampleproject;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

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

    public static final float SOUND_VOLUME = 0.5f;
    public static final float FX_VOLUME = 1.0f;
    private static final int FX_AUDIO_STREAMS = 3;
    public MediaPlayer mediaPlayer = null;
    public SoundPool soundPool = null;
    public int beepSound, peepSound;

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

    public void init() {
        initPlayer();

        initPlayerDatabase();
        initStatusDatabase();

        // sound setup
        initSound();
    }

    private void initSound() {

        mediaPlayer = MediaPlayer.create(context, R.raw.creepy);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(SOUND_VOLUME, SOUND_VOLUME);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attrs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attrs)
                    .setMaxStreams(FX_AUDIO_STREAMS)
                    .build();
        } else {
            soundPool = new SoundPool(FX_AUDIO_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        beepSound = soundPool.load(context, R.raw.beeep, 1);
        peepSound = soundPool.load(context, R.raw.peeeeeep, 1);

    }

    public void releaseSound() {
        mediaPlayer.release();
        soundPool.release();
    }

    public void stopSound() {
        mediaPlayer.stop();
    }

    public void playSound() {
        mediaPlayer.start();
    }

    private void initPlayer() {
        players.clear();
        Player player = new Player(ID,0.5,0.5);
        players.put(ID, player);
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
                    Log.d(TAG, player.toString());
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

        // check exit?
        for (Player p: Game.getInstance().getPlayers()) {
            if (p.checkExit(board)) {
                setWinner(p);
                break;
            }
        }
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
        // make sound effect
        switch (GameMetadata.GameStatus.valueOf(status)) {
            case PAUSED:
                soundPool.play(peepSound, FX_VOLUME, FX_VOLUME, 0, 0, 1);
                Toast.makeText(context, "GAME PAUSED", Toast.LENGTH_LONG).show();
                break;
            case RUNNING:
                soundPool.play(beepSound, FX_VOLUME, FX_VOLUME, 0, 0, 1);
                Toast.makeText(context, "GAME RESUMED", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
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

    private void setWinner(Player p) {
        // TODO set winner value in DB
        statusDatabase.setValue(FINISHED);
    }
}
