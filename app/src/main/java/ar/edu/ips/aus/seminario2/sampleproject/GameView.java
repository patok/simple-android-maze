package ar.edu.ips.aus.seminario2.sampleproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.abemart.wroup.client.WroupClient;
import com.abemart.wroup.common.messages.MessageWrapper;

import java.util.Random;
import java.util.Vector;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameAnimationThread thread;
    private Player player;
    private Vector<Player> players = new Vector<>();
    private PlayerSprite playerSprites;

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
        getHolder().addCallback(this);

        String id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        player = new Player(id,0.5,0.5);
        Player player2 = new Player("NNNN", 8.5, 8.5);
        player2.setNewDirection(MazeBoard.Direction.NORTH);
        Player player3 = new Player("ZZZZ", 0, 8.5);
        player3.setNewDirection(MazeBoard.Direction.NORTH);

        players.add(player);
        players.add(player2);
        players.add(player3);

        playerSprites = new PlayerSprite(getResources());

        thread = new GameAnimationThread(getHolder(), this);
        setFocusable(true);
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //init();
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
        // TODO update all players
        MazeBoard board = GameApp.getInstance().getMazeBoard();
        Random rand = new Random();
        MazeBoard.Direction[] values = MazeBoard.Direction.values();
        for (Player p:this.players) {
            // randomly update other players
            if (p != this.player &&
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

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if (canvas != null){
            MazeBoard board = GameApp.getInstance().getMazeBoard();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            int count = 0;
            for (Player p:this.players) {
                Rect srcRect = playerSprites.getSourceRectangle(this, board, p, count);
                Rect dstRect = playerSprites.getDestinationRectangle(this, board, p);
                Log.d("MAZE: ", String.format("src rect: %s - dst rect: %s", srcRect.toShortString(), dstRect.toShortString()));
                canvas.drawBitmap(playerSprites.getSprites(), srcRect, dstRect, null);
                count++;
            }
        }
    }
}
