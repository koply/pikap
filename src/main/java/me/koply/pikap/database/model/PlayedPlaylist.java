package me.koply.pikap.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;

@DatabaseTable(tableName = "playedPlaylists")
public class PlayedPlaylist {

    @DatabaseField(generatedId = true, columnName = "id")
    private int id;

    public PlayedPlaylist() { }

    public PlayedPlaylist(Playlist playlist, Track firstTrack) {
        this.playlist = playlist;
        this.lastTrack = firstTrack;
        this.lastTrackIndex = 0;
    }

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Playlist playlist;

    @DatabaseField private int lastTrackIndex;
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Track lastTrack;

    @DatabaseField private Timestamp playedTimestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public int getLastTrackIndex() {
        return lastTrackIndex;
    }

    public void setLastTrackIndex(int lastTrackIndex) {
        this.lastTrackIndex = lastTrackIndex;
    }

    public Track getLastTrack() {
        return lastTrack;
    }

    public void setLastTrack(Track lastTrack) {
        this.lastTrack = lastTrack;
    }

    public Timestamp getPlayedTimestamp() {
        return playedTimestamp;
    }

    public void setPlayedTimestamp(Timestamp playedTimestamp) {
        this.playedTimestamp = playedTimestamp;
    }
}
