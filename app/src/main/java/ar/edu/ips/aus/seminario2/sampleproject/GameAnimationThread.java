package ar.edu.ips.aus.seminario2.sampleproject;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameAnimationThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running = false;
    public static Canvas canvas;
    private DatabaseReference statusDBReference;
    private ValueEventListener statusValueListener;

    public GameAnimationThread(SurfaceHolder surfaceHolder, GameView gameView){
        super();

        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    @Override
    public void run() {
        final int COUNT_INTERVAL = 20;
        long startWhen = System.nanoTime();
        int intervalCount = 0;

        // init status listener in order to follow game status
        String path = String.format("/%s/status",Game.getInstance().getGameMetadata().getId());
        statusDBReference = FirebaseDatabase.getInstance().getReference(path);
        statusValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    GameMetadata.GameStatus status = snapshot.getValue(GameMetadata.GameStatus.class);
                    Game.getInstance().setStatus(status.toString());
                } catch (Exception e) {
                    // intentionally left blank
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("GAT", "onCancelled", error.toException());
            }
        };
        statusDBReference.addValueEventListener(statusValueListener);

        while (running){
            canvas = null;
            // Game status logic
            try {
                if (Game.getInstance().getStatus() == GameMetadata.GameStatus.RUNNING) {
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder){
                        Game.getInstance().update();
                        this.gameView.draw(canvas);
                    }
                }
                Thread.sleep(10);
                intervalCount++;
                if (COUNT_INTERVAL <= intervalCount){
                    long now = System.nanoTime();
                    double framesPerSec = 1000000000.0 / ((now - startWhen) / (intervalCount));
                    Log.d("FPS", String.format("%2.2f - status: %s", framesPerSec, Game.getInstance().getStatus()));
                    startWhen = now;
                    intervalCount = 0;
                }
            } catch (Exception e){
                Log.d("GAT", e.getMessage());
            } finally {
                if (canvas != null ){
                    try {
                        this.surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e){
                        Log.d("GAT", e.getMessage());
                    }
                }
            }
        }

        // remove status listeners
        statusDBReference.removeEventListener(statusValueListener);
        Log.d("GAT", "Exit GAT!");
    }

    public void setRunning(boolean isRunning) {
        this.running = isRunning;
    }
}
