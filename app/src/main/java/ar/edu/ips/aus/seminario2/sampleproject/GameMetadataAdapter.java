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

/**
 * see sample code in https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
 */
public class GameMetadataAdapter
        extends RecyclerView.Adapter<GameMetadataAdapter.GameSelectionViewHolder>{

    private List<GameMetadata> gameList;
    private DatabaseReference mDatabaseReference;
    private ItemClickListener mClickListener;

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

    public class GameSelectionViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView gameTitle;

        public GameSelectionViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.gameTitle);
            itemView.setOnClickListener(this);
        }

        public void bind(GameMetadata data){
            gameTitle.setText(data.getTitle());
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int pos) {
        return gameList.get(pos).getTitle();
    }

    public List<GameMetadata> getData() {
        return gameList;
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
