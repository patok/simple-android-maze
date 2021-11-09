package ar.edu.ips.aus.seminario2.sampleproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class GameSelectionActivity extends AppCompatActivity
        implements GameMetadataAdapter.ItemClickListener {

    private GameMetadataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);

        RecyclerView recyclerView = findViewById(R.id.gameListView);
        adapter = new GameMetadataAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager gameListLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gameListLayoutManager);

        adapter.setClickListener(this);

        ImageButton newGameButton = findViewById(R.id.newGame);
        EditText gameNameInput = findViewById(R.id.gameName);

        newGameButton.setOnClickListener(view -> {
            // push gameMetadata
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            GameMetadata data = new GameMetadata();
            String dataId = databaseReference.push().getKey();
            data.setId(dataId);
            data.setTitle(gameNameInput.getText().toString());
            data.setGameBoard(MazeBoard.from("asdasd"));
            data.setStatus(GameMetadata.GameStatus.NEW.name());
            data.setAuthor(Game.getInstance(this.getApplicationContext()).ID);

            databaseReference.child(dataId).setValue(data);

            Game.getInstance(GameSelectionActivity.this).setGameMetadata(data);

            startNewGameActivity();
        });
        Game.getInstance(this.getApplicationContext());
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getData().get(position).getId() + " on row number " + position, Toast.LENGTH_SHORT).show();
        GameMetadata metadata = adapter.getData().get(position);
        Game.getInstance(this).setGameMetadata(metadata);

        startNewGameActivity();
    }

    private void startNewGameActivity() {
        Intent activityIntent = new Intent(this, MazeBoardActivity.class);
        startActivity(activityIntent);
    }
}