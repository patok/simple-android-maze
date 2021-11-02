package ar.edu.ips.aus.seminario2.sampleproject;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameAnimationThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    public static Canvas canvas;

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

        while (running){
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    Game.getInstance().update();
                    this.gameView.draw(canvas);
                }
                Thread.sleep(10);
                intervalCount++;
                if (COUNT_INTERVAL <= intervalCount){
                    long now = System.nanoTime();
                    double framesPerSec = 1000000000.0 / ((now - startWhen) / (intervalCount));
                    Log.d("FPS", String.format("%2.2f", framesPerSec));
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
    }

    public void setRunning(boolean isRunning) {
        this.running = isRunning;
    }
}
