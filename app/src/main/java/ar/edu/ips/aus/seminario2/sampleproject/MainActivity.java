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

    private int playerX = 2, playerY = 1;
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
        imageViews[(playerX%board.getWidth())+ board.getHeight()*playerY].setImageResource(android.R.drawable.ic_menu_add);
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
                if (piece.openTo(BoardPiece.Direction.WEST)){
                    if (piece.openTo(BoardPiece.Direction.NORTH)){
                        if (piece.openTo(BoardPiece.Direction.EAST)){
                            if (piece.openTo(BoardPiece.Direction.SOUTH)){
                                resId = R.drawable.m4;
                            }
                            else {
                                resId = R.drawable.m3b;
                            }
                        } else {
                            if (piece.openTo(BoardPiece.Direction.SOUTH)) {
                                resId = R.drawable.m3r;
                            }
                            else {
                                resId = R.drawable.m2lt;
                            }
                        }
                    }
                    else {
                        if (piece.openTo(BoardPiece.Direction.EAST)){
                            if (piece.openTo(BoardPiece.Direction.SOUTH)){
                                resId = R.drawable.m3t
                                ;
                            }
                            else {
                                resId = R.drawable.m2h;
                            }
                        } else {
                            if (piece.openTo(BoardPiece.Direction.SOUTH)) {
                                resId = R.drawable.m2bl;                                ;
                            }
                            else {
                                resId = R.drawable.m1l;
                            }
                        }

                    }
                } else {
                    if (piece.openTo(BoardPiece.Direction.NORTH)){
                        if (piece.openTo(BoardPiece.Direction.EAST)){
                            if (piece.openTo(BoardPiece.Direction.SOUTH)){
                                resId = R.drawable.m3l;
                            }
                            else {
                                resId = R.drawable.m2tr;
                            }
                        } else {
                            if (piece.openTo(BoardPiece.Direction.SOUTH)) {
                                resId = R.drawable.m2v;
                            }
                            else {
                                resId = R.drawable.m1t;
                            }
                        }
                    }
                    else {
                        if (piece.openTo(BoardPiece.Direction.EAST)){
                            if (piece.openTo(BoardPiece.Direction.SOUTH)){
                                resId = R.drawable.m2rb;
                            }
                            else {
                                resId = R.drawable.m1r;
                            }
                        } else {
                            if (piece.openTo(BoardPiece.Direction.SOUTH)) {
                                resId = R.drawable.m1b;
                            }
                            else {
                                throw new RuntimeException("Maze piece not recognized!");
                            }
                        }
                    }
                }

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

    @Override
    public void onClick(View v) {
        // complete with event listener logic
        Log.d("MAZE", String.format("coords: %d %d ", playerX, playerY));
        if ((playerX >= 0 & playerX < board.getWidth() ) & (playerY >=0 & playerY < board.getHeight())) {
            imageViews[(playerX%board.getWidth())+ board.getHeight()*playerY].setImageDrawable(null);
        }

        if (v == buttonUp) {
            playerY = (playerY == 0 ? 0 : playerY - 1);
        }
        if (v == buttonDown) {
            playerY = (playerY == board.getHeight()-1 ? board.getHeight()-1 : playerY + 1);
        }
        if (v == buttonLeft) {
            playerX = (playerX == 0 ? 0: playerX - 1);
        }
        if (v == buttonRight) {
            playerX = (playerX == board.getWidth()-1 ? board.getWidth()-1: playerX + 1);
        }

        if ((playerX >= 0 & playerX < board.getWidth() ) &&
                (playerY >=0 & playerY < board.getHeight())) {
            imageViews[(playerX%board.getWidth())+ board.getHeight()*playerY].setImageResource(android.R.drawable.ic_menu_add);
        }
        Log.d("MAZE", String.format("coords: %d %d ", playerX, playerY));
    }
}
