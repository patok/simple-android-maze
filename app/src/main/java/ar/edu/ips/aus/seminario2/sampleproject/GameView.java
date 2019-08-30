package ar.edu.ips.aus.seminario2.sampleproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameAnimationThread thread;

    private MazeBoard board;
    private Bitmap playerImage;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GameView(Context context){
        super(context);
        init();
    }

    private void init() {
        board = MazeBoard.from("some repl");
        getHolder().addCallback(this);

        playerImage = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_add);

        thread = new GameAnimationThread(getHolder(), this);
        setFocusable(true);
    }

    public MazeBoard getBoard() {
        return board;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry){
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {
        board.update();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if (canvas != null){
            playerImage.getWidth();
            playerImage.getHeight();
            long tileWidth = this.getWidth()/board.getWidth();
            long tileHeight = this.getHeight()/board.getHeight();
            long x = board.getPlayer().getX() * tileWidth + (tileWidth - playerImage.getWidth()) / 2;
            long y = board.getPlayer().getY() * tileHeight + (tileHeight - playerImage.getHeight())/ 2;
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawBitmap(playerImage, x, y, null);
        }
    }
}
