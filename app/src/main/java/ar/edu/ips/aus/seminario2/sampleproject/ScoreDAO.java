package ar.edu.ips.aus.seminario2.sampleproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ScoreDAO {

    @Insert
    void insert(Score score);

    @Update
    void update(Score score);

    @Delete
    void delete(Score score);

    @Query("SELECT * FROM score WHERE player_id = :id")
    Score findScoreById(String id);

    @Query("SELECT * FROM score ORDER BY points DESC")
    List<Score> getAll();
}
