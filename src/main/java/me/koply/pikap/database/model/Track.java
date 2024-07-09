package me.koply.pikap.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
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
    @DatabaseField private boolean favourite;


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

    public Track() {
        this("", "", 0, null);
    }

    public void increaseListenedTimes() {
        listenedTimes++;
    }

}