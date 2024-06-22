package me.koply.pikap.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import java.sql.Timestamp;

@DatabaseTable(tableName = "tracks")
public class Track {

    @DatabaseField(generatedId = true, columnName = "id")
    private int id;

    // TODO MAX LENGTH
    @DatabaseField private String title; // VARCHAR(100)
    @DatabaseField private String author; // VARCHAR(32)
    @DatabaseField private String identifier; // VARCHAR(16)
    @DatabaseField private long duration;
    @DatabaseField private int listenedTimes;
    @DatabaseField private long lastMillis; // it'll be 0 if there is no remaining part
    @DatabaseField private Timestamp lastPlayed;
    @DatabaseField private Timestamp firstPlayed;

    @DatabaseField(canBeNull = true)
    private String playlistIds;

    public String getPlaylistIdsString() {
        return playlistIds;
    }

    public int[] getPlaylistIds() {
        String[] ids = playlistIds.split(",");
        int[] idsArray = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            idsArray[i] = Integer.parseInt(ids[i]);
        }
        return idsArray;
    }

    public void addPlaylistId(int id) {
        playlistIds += "," + id;
    }

    // identifier is the YouTube key of the track
    public Track(int id, String title, String author, long duration, String identifier) {
        this(title, author, duration, identifier);
        this.id = id;
    }

    public Track(String title, String author, long duration, String identifier) {
        this.title = title.length() > 64 ? title.substring(0,64) : title;
        this.author = author.length() > 32 ? author.substring(0,32) : author;
        this.duration = duration;
        this.identifier = identifier;
    }

    public Track(AudioTrackInfo info) {
        this(info.title, info.author, info.length, info.identifier);
    }

    public Track() { }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Track setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Track setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Track setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public Track setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public void increaseListenedTimes() {
        listenedTimes++;
    }

    public int getListenedTimes() {
        return listenedTimes;
    }

    public void setListenedTimes(int listenedTimes) {
        this.listenedTimes = listenedTimes;
    }

    public long getLastMillis() {
        return lastMillis;
    }

    public void setLastMillis(long lastMillis) {
        this.lastMillis = lastMillis;
    }

    public Timestamp getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(Timestamp lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public Timestamp getFirstPlayed() {
        return firstPlayed;
    }

    public void setFirstPlayed(Timestamp firstPlayed) {
        this.firstPlayed = firstPlayed;
    }
}