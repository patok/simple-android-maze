package ar.edu.ips.aus.seminario2.sampleproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final int SPRITE_HEIGHT = 72;
    private static final int SPRITE_WIDTH = 52;
    private GameAnimationThread thread;

    private MazeBoard board;
    private Bitmap playerSprites;

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

        playerSprites = BitmapFactory.decodeResource(getResources(), R.drawable.characters);

        Log.d("BITMAP:", String.format("metadata -bytes: %d - size: %d x %d",
                playerSprites.getByteCount(), playerSprites.getWidth(), playerSprites.getHeight()));

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
        Log.d("MOVE:", String.format("position: %2.2f,%2.2f", this.board.getPlayer().getX(), this.board.getPlayer().getY()));
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if (canvas != null){
            long tileWidth = this.getWidth()/board.getWidth();
            long tileHeight = this.getHeight()/board.getHeight();
            float x = (float) (board.getPlayer().getX() * tileWidth) - (SPRITE_WIDTH/2);
            float y = (float) (board.getPlayer().getY() * tileHeight) - (SPRITE_HEIGHT/ 2);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            int srcTop = 0;
            int srcLeft = 0;
            int srcRight = 0;
            int srcBottom = 0;
            int[] spriteOffset = { SPRITE_WIDTH *6, SPRITE_WIDTH*7, SPRITE_WIDTH*8, SPRITE_WIDTH*7, SPRITE_WIDTH*6};
            switch (board.getPlayerDirection()){
                case WEST:
                    srcTop = SPRITE_HEIGHT;
                    srcLeft = spriteOffset[(int) (x % spriteOffset.length)];
                    break;
                case NORTH:
                    srcTop = SPRITE_HEIGHT * 3;
                    srcLeft = spriteOffset[(int) (y % spriteOffset.length)];
                    break;
                case EAST:
                    srcTop = SPRITE_HEIGHT * 2;
                    srcLeft = spriteOffset[(int) (x % spriteOffset.length)];
                    break;
                case SOUTH:
                    srcTop = 0;
                    srcLeft = spriteOffset[(int) (y % spriteOffset.length)];
                    break;
            }
            srcBottom = srcTop + SPRITE_HEIGHT;
            srcRight = srcLeft + SPRITE_WIDTH;
            Rect srcRect = new Rect(srcLeft, srcTop, srcRight, srcBottom);

            Rect dstRect = new Rect((int)x,(int)y,(int)x+SPRITE_WIDTH,(int)y+SPRITE_HEIGHT);
            //Log.d("MAZE: ", String.format("src rect: %s - dst rect: %s", srcRect.toShortString(), dstRect.toShortString()));

            canvas.drawBitmap(playerSprites, srcRect, dstRect, null);

        }
    }
}
