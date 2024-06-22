package me.koply.pikap.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import me.koply.pikap.util.Util;

import java.sql.Timestamp;
import java.util.List;

@DatabaseTable(tableName = "playlists")
public class Playlist {

    public Playlist() { }

    public Playlist(AudioPlaylist audioPlaylist, String youtubeIdentifier, long totalDuration) {
        this.name = audioPlaylist.getName();
        this.totalDuration = totalDuration;
        this.youtubeIdentifier = youtubeIdentifier;
    }

    @DatabaseField(generatedId = true, columnName = "id")
    private int id;

    @DatabaseField private String name;
    @DatabaseField private long totalDuration;
    @DatabaseField private Timestamp createdAt;

    @DatabaseField(canBeNull = true)
    private String youtubeIdentifier;

    @DatabaseField
    private String trackIds;

    public String getTrackIdsString() {
        return trackIds;
    }

    public int[] getTrackIds() {
        if (trackIds == null) {
            return new int[0];
        }

        String[] splitted = trackIds.split(",");
        int[] ids = new int[splitted.length];
        for (int i = 0; i < splitted.length; i++) {
            Integer parsed = Util.parseInt(splitted[i]);
            if (parsed == null) continue;
            ids[i] = parsed;
        }
        return ids;
    }

    public void addTrackId(int trackId) {
        trackIds += "," + trackId;
    }

    public void setTrackIds(String trackIds) {
        this.trackIds = trackIds;
    }

    public void setTrackIds(List<Integer> trackIds) {
        StringBuilder sb = new StringBuilder();
        int j = 0;
        for (Integer i : trackIds) {
            sb.append(i);
            if (++j != trackIds.size()) {
                sb.append(",");
            }
        }
        this.trackIds = sb.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getYoutubeIdentifier() {
        return youtubeIdentifier;
    }

    public void setYoutubeIdentifier(String youtubeIdentifier) {
        this.youtubeIdentifier = youtubeIdentifier;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
