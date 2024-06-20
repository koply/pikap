package me.koply.pikap.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

@DatabaseTable(tableName = "favourites")
public class FavouriteTrack {

    @DatabaseField(generatedId = true, columnName = "id")
    private int id;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Track track;

    public FavouriteTrack(AudioTrackInfo info) {
        track = new Track(info);
    }

    public FavouriteTrack(Track track) {
        this.track = track;
    }

    public FavouriteTrack() {}

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public int getId() {
        return id;
    }

}
