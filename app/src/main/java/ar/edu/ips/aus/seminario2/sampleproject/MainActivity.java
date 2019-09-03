package ar.edu.ips.aus.seminario2.sampleproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonUp, buttonDown, buttonLeft, buttonRight;

    ImageView[] imageViews = null;

    private MazeBoard board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maze);

        buttonUp = findViewById(R.id.buttonUp);
        buttonDown = findViewById(R.id.buttonDown);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonRight = findViewById(R.id.buttonRight);

        buttonUp.setOnClickListener(this);
        buttonDown.setOnClickListener(this);
        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);

        setupMazeBoard();

        setupPlayerToken();
    }

    private void setupPlayerToken() {
        imageViews[(board.getPlayer().getX()%board.getWidth())+ board.getHeight()*board.getPlayer().getY()].
                setImageResource(android.R.drawable.ic_menu_add);
    }

    private void setupMazeBoard() {

        board = MazeBoard.from("empty");
        int height = board.getHeight();
        int width = board.getWidth();

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

                imageViews[(j%board.getWidth())+ board.getHeight()*i] = imageView;
            }
        }
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
        // complete with event listener logic
        Log.d("MAZE", String.format("coords: %d %d ", board.getPlayer().getX(), board.getPlayer().getY()));

        imageViews[(board.getPlayer().getX()%board.getWidth())+ board.getHeight()*board.getPlayer().getY()].
                setImageDrawable(null);

        if (v == buttonUp) {
            board.movePlayer(MazeBoard.Direction.NORTH);
        }
        else if (v == buttonDown) {
            board.movePlayer(MazeBoard.Direction.SOUTH);
        }
        else if (v == buttonLeft) {
            board.movePlayer(MazeBoard.Direction.WEST);
        }
        else if (v == buttonRight) {
            board.movePlayer(MazeBoard.Direction.EAST);
        }

        imageViews[(board.getPlayer().getX()%board.getWidth())+ board.getHeight()*board.getPlayer().getY()].
                setImageResource(android.R.drawable.ic_menu_add);

        Log.d("MAZE", String.format("coords: %d %d ", board.getPlayer().getX(), board.getPlayer().getY()));
    }
}
