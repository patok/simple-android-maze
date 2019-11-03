package ar.edu.ips.aus.seminario2.sampleproject;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.abemart.wroup.client.WroupClient;
import com.abemart.wroup.common.WroupDevice;
import com.abemart.wroup.common.listeners.ClientConnectedListener;
import com.abemart.wroup.common.listeners.ClientDisconnectedListener;
import com.abemart.wroup.common.listeners.DataReceivedListener;
import com.abemart.wroup.common.messages.MessageWrapper;
import com.abemart.wroup.service.WroupService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import static ar.edu.ips.aus.seminario2.sampleproject.Message.MessageType.PLAYER_DATA;

public class MazeBoardActivity extends AppCompatActivity
        implements View.OnClickListener, DataReceivedListener,
        ClientConnectedListener, ClientDisconnectedListener,
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    public static final String EXTRA_SERVER_NAME = "SERVER_NAME";
    public static final String EXTRA_IS_SERVER = "IS_SERVER";
    private static final String TAG = MazeBoardActivity.class.getSimpleName();
    private static final int MAX_DEVICES = 3;
    private static final int SWIPE_TRESHOLD = 100;
    private static final int SWIPE_VELOCITY_TRESHOLD = 100;
    private GestureDetectorCompat gestureDetector;

    private Button buttonUp, buttonDown, buttonLeft, buttonRight, buttonPause;

    ImageView[] imageViews = null;

    private GameView mazeView;
    private final HashMap<String, WroupDevice> devices = new HashMap<String, WroupDevice>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maze);

        buttonUp = findViewById(R.id.buttonUp);
        buttonDown = findViewById(R.id.buttonDown);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonRight = findViewById(R.id.buttonRight);
        buttonPause = findViewById(R.id.buttonPause);

        buttonUp.setOnClickListener(this);
        buttonDown.setOnClickListener(this);
        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);
        buttonPause.setOnClickListener(this);

        mazeView = (GameView)findViewById(R.id.gameView);
        mazeView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mazeView.setZOrderMediaOverlay(true);
        mazeView.setZOrderOnTop(true);

        // TODO setup global variables earlier ?
        GameApp.getInstance().setServerName(getIntent().getStringExtra(this.EXTRA_SERVER_NAME));
        GameApp.getInstance().setGameServer(getIntent().getBooleanExtra(this.EXTRA_IS_SERVER, false));
        Log.d(TAG, "Is game server? : " + GameApp.getInstance().isGameServer());

        if (GameApp.getInstance().isGameServer()){
            WroupService server = GameApp.getInstance().getServer();
            server.setDataReceivedListener(this);
            server.setClientDisconnectedListener(this);
            server.setClientConnectedListener(this);
            GameApp.getInstance().setServer(server);
        } else {
            WroupClient client = GameApp.getInstance().getClient();
            client.setDataReceivedListener(this);
            client.setClientDisconnectedListener(this);
            client.setClientConnectedListener(this);
            GameApp.getInstance().setClient(client);
        }

        if (GameApp.getInstance().isGameServer()) {
            MazeBoard board = MazeBoard.from("asdasd");
            GameApp.getInstance().setMazeBoard(board);
            setupMazeBoard(board);
        }

        gestureDetector = new GestureDetectorCompat(this, this);
    }

    private void setupMazeBoard(MazeBoard board) {

        int height = board.getVerticalTileCount();
        int width = board.getHorizontalTileCount();

        imageViews = new ImageView[width * height];

        int resId = 0;

        TableLayout table = findViewById(R.id.mazeBoard);
        for (int i=0; i<height; i++){
            TableRow row = new TableRow(this);
            TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams();
            rowParams.width = TableRow.LayoutParams.MATCH_PARENT;
            rowParams.height = TableRow.LayoutParams.MATCH_PARENT;
            rowParams.weight = 1;
            rowParams.gravity = Gravity.CENTER;
            row.setLayoutParams(rowParams);
            table.addView(row);

            for (int j=0; j<width; j++){
                BoardPiece piece = board.getPiece(j, i);

                resId = lookupResource(piece);

                ImageView imageView = new ImageView(this);
                imageView.setBackgroundResource(resId);
                TableRow.LayoutParams imageViewParams = new TableRow.LayoutParams();
                imageViewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                imageViewParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                imageViewParams.weight = 1;
                imageView.setLayoutParams(imageViewParams);

                row.addView(imageView);

                imageViews[(j%board.getHorizontalTileCount())+ board.getVerticalTileCount()*i] = imageView;
            }
        }
        table.invalidate();
   }

    private int lookupResource(BoardPiece piece) {
        int iconIndex = 0b1000 * (piece.isOpen(MazeBoard.Direction.WEST)? 1:0) +
                0b0100 * (piece.isOpen(MazeBoard.Direction.NORTH)? 1:0) +
                0b0010 * (piece.isOpen(MazeBoard.Direction.EAST)? 1:0) +
                0b0001 * (piece.isOpen(MazeBoard.Direction.SOUTH)? 1:0);

        int[] iconLookupTable = { 0,
                R.drawable.m1b,
                R.drawable.m1r,
                R.drawable.m2rb,
                R.drawable.m1t,
                R.drawable.m2v,
                R.drawable.m2tr,
                R.drawable.m3l,
                R.drawable.m1l,
                R.drawable.m2bl,
                R.drawable.m2h,
                R.drawable.m3t,
                R.drawable.m2lt,
                R.drawable.m3r,
                R.drawable.m3b,
                R.drawable.m4};

        return iconLookupTable[iconIndex];
    }

    @Override
    public void onClick(View v) {
        if (v == buttonUp) {
            mazeView.getPlayer().setNewDirection(MazeBoard.Direction.NORTH);
        }
        else if (v == buttonDown) {
            mazeView.getPlayer().setNewDirection(MazeBoard.Direction.SOUTH);
        }
        else if (v == buttonLeft) {
            mazeView.getPlayer().setNewDirection(MazeBoard.Direction.WEST);
        }
        else if (v == buttonRight) {
            mazeView.getPlayer().setNewDirection(MazeBoard.Direction.EAST);
        } else if (v == buttonPause) {
            mazeView.toggleStatus();
        }

    }

    @Override
    public void onClientConnected(final WroupDevice wroupDevice) {
        if (GameApp.getInstance().isGameServer()) {
            addToDeviceList(wroupDevice);
            Log.d(TAG, "Client connected : " + wroupDevice.getDeviceName());
            // send mazeBoard instance to connected client
            WroupService server = GameApp.getInstance().getServer();
            MessageWrapper message = new MessageWrapper();
            // FIXME replace w/ MazeBoard.toString() call
            Gson json = new Gson();
            Message<MazeBoard> data = new Message<MazeBoard>(Message.MessageType.GAME_DATA,
                    GameApp.getInstance().getMazeBoard());
            String msg = json.toJson(data);
            message.setMessage(msg);
            message.setMessageType(MessageWrapper.MessageType.NORMAL);
            // FIXME find another way to make sure MazeBoardActivity
            //  & listeners are up
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            server.sendMessage(wroupDevice, message);
            Log.d(TAG, "Sending maze board data to new client " + wroupDevice.getDeviceName());

        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getString(R.string.device_connected, wroupDevice.getDeviceName()), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClientDisconnected(final WroupDevice wroupDevice) {
        if (GameApp.getInstance().isGameServer()) {
            removeFromDeviceList(wroupDevice);
            Log.d(TAG, "Client dis-connected : " + wroupDevice.getDeviceName());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getString(R.string.device_disconnected, wroupDevice.getDeviceName()), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean addToDeviceList(WroupDevice wroupDevice) {
        if (devices.size() < MAX_DEVICES) {
            devices.put(wroupDevice.getDeviceMac(), wroupDevice);
            return true;
        }
        return false;
    }

    private boolean removeFromDeviceList(WroupDevice wroupDevice) {
        return (devices.remove(wroupDevice.getDeviceMac()) != null);
    }

    @Override
    public void onDataReceived(MessageWrapper messageWrapper) {
        if (!GameApp.getInstance().isGameServer()) {
            // client may receive different kind of messages from server
            JsonObject object = JsonParser.parseString(messageWrapper.getMessage()).getAsJsonObject();
            JsonElement typeElement = object.get("type");
            switch (Message.MessageType.valueOf(typeElement.getAsString())){
                case PLAYER_DATA:
                    mazeView.updatePlayerData(messageWrapper.getMessage());
                    break;
                case GAME_DATA:
                    // FIXME replace w/ MazeBoard.from() call
                    Gson gson = new Gson();
                    final Message<MazeBoard> data = gson.fromJson(messageWrapper.getMessage(),
                            new TypeToken<Message<MazeBoard>>(){}.getType());
                    final MazeBoard board = data.getPayload();
                    GameApp.getInstance().setMazeBoard(board);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setupMazeBoard(board);
                        }
                    });
                    Log.d(TAG, "Received maze board data!");
                    break;
                case GAME_STATUS:
                    mazeView.updateStatus(messageWrapper.getMessage());
                    break;
            }
        } else {
            // server receives individual player data
            if (devices.containsKey(messageWrapper.getWroupDevice().getDeviceMac())){
                mazeView.updatePlayerData(messageWrapper.getMessage());
            }
        }

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        mazeView.toggleStatus();
        return true;
    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velX, float velY) {
        boolean eventConsumed = false;
        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX = moveEvent.getX() - downEvent.getX();

        if (Math.abs(diffX) > Math.abs(diffY)){
            if (Math.abs(diffX) > SWIPE_TRESHOLD && Math.abs(velX) > SWIPE_VELOCITY_TRESHOLD) {
                if (diffX > 0) {
                    onSwipeRight();
                } else {
                    onSwipeLeft();
                }
                eventConsumed = true;
            }
        } else {
            if (Math.abs(diffY) > SWIPE_TRESHOLD && Math.abs(velY) > SWIPE_VELOCITY_TRESHOLD){
                if (diffY > 0) {
                    onSwipeBottom();
                } else {
                    onSwipeTop();
                }
                eventConsumed = true;
            }
        }
        return eventConsumed;
    }

    private void onSwipeTop() {
        mazeView.getPlayer().setNewDirection(MazeBoard.Direction.NORTH);
    }

    private void onSwipeBottom() {
        mazeView.getPlayer().setNewDirection(MazeBoard.Direction.SOUTH);
    }

    private void onSwipeLeft() {
        mazeView.getPlayer().setNewDirection(MazeBoard.Direction.WEST);
    }

    private void onSwipeRight() {
        mazeView.getPlayer().setNewDirection(MazeBoard.Direction.EAST);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
            return true;
        return super.onTouchEvent(event);
    }
}
