package ar.edu.ips.aus.seminario2.sampleproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class GameSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);

        RecyclerView recyclerView = findViewById(R.id.gameListView);
        final GameMetadataAdapter adapter = new GameMetadataAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager gameListLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gameListLayoutManager);
    }
}