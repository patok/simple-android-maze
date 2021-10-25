package ar.edu.ips.aus.seminario2.sampleproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GameMetadataAdapter
        extends RecyclerView.Adapter<GameMetadataAdapter.GameSelectionViewHolder>{

    private List<GameMetadata> gameList;
    private DatabaseReference mDatabaseReference;

    public GameMetadataAdapter() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        gameList = new ArrayList<>();
        ChildEventListener mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GameMetadata data = snapshot.getValue(GameMetadata.class);
                Log.d("Game: ", data.getTitle());
                data.setId(snapshot.getKey());
                gameList.add(data);
                notifyItemInserted(gameList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @NonNull
    @Override
    public GameSelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.game_selection_row, parent, false);
        return new GameSelectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GameSelectionViewHolder holder, int position) {
        GameMetadata gameMetadata = gameList.get(position);
        holder.bind(gameMetadata);
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public class GameSelectionViewHolder extends RecyclerView.ViewHolder {
        TextView gameTitle;

        public GameSelectionViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.gameTitle);
        }

        public void bind(GameMetadata data){
            gameTitle.setText(data.getTitle());
        }
    }
}
