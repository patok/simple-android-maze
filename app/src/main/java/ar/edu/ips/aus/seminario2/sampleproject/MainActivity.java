package ar.edu.ips.aus.seminario2.sampleproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonUp, buttonDown, buttonLeft, buttonRight;

    ImageView[] imageViews = null;

    private int x = 0, y = 0;

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

        imageViews = new ImageView[9];
        imageViews[0] = findViewById(R.id.imageView);
        imageViews[1] =  findViewById(R.id.imageView7);
        imageViews[2] = findViewById(R.id.imageView2);
        imageViews[3] = findViewById(R.id.imageView9);
        imageViews[4] = findViewById(R.id.imageView10);
        imageViews[5] = findViewById(R.id.imageView11);
        imageViews[6] = findViewById(R.id.imageView3);
        imageViews[7] = findViewById(R.id.imageView8);
        imageViews[8] = findViewById(R.id.imageView4);
    }

    @Override
    public void onClick(View v) {
        // complete with event listener logic
        Log.d("MAZE", String.format("coords: %d %d ", x, y));
        if ((x >= 0 & x < 3 ) & (y>=0 & y < 3)) {
            imageViews[3*(y%3)+x].setImageDrawable(null);
        }

        if (v == buttonUp) {
            y = (y == 0 ? 0 : y - 1);
        }
        if (v == buttonDown) {
            y = (y == 2 ? 2 : y + 1);
        }
        if (v == buttonLeft) {
            x = (x == 0 ? 0: x - 1);
        }
        if (v == buttonRight) {
            x = (x == 2 ? 2: x + 1);
        }

        if ((x >= 0 & x < 3 ) & (y>=0 & y < 3)) {
            imageViews[3*(y%3)+x].setImageResource(android.R.drawable.ic_menu_add);
        }
        Log.d("MAZE", String.format("coords: %d %d ", x, y));
    }
}
