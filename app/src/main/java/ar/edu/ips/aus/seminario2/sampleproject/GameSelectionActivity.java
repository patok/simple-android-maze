package ar.edu.ips.aus.seminario2.sampleproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class GameSelectionActivity extends AppCompatActivity
        implements GameMetadataAdapter.ItemClickListener {

    private GameMetadataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);

        // TODO move to other lifecycle methods?
        RecyclerView recyclerView = findViewById(R.id.gameListView);
        adapter = new GameMetadataAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager gameListLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gameListLayoutManager);

        adapter.setClickListener(this);

        Game.getInstance(this.getApplicationContext());
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getData().get(position).getId() + " on row number " + position, Toast.LENGTH_SHORT).show();
        GameMetadata metadata = adapter.getData().get(position);
        Game.getInstance(this).setGameMetadata(metadata);

        Intent activityIntent = new Intent(this, MazeBoardActivity.class);
        startActivity(activityIntent);
    }
}