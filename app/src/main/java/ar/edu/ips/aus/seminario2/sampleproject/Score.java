package ar.edu.ips.aus.seminario2.sampleproject;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "score")
public class Score {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "player_id")
    private String playerId;

    @ColumnInfo(name = "player_name")
    private String playerName;
    @ColumnInfo(name = "device")
    private String deviceName;
    @ColumnInfo(name = "points")
    private int points;

    public void Score(String id, String device, int points) {
        this.playerId = id;
        this.deviceName = device;
        this.points = points;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
