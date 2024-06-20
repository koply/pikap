package me.koply.pikap.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

@DatabaseTable(tableName = "tracks")
public class Track {

    // unused
    private static final String CREATE_TRACKS_TABLE = "CREATE TABLE [IF NOT EXISTS] knowntracks (\n" +
            "id INTEGER PRIMARY KEY,\n" +
            "title VARCHAR(64),\n" +
            "author VARCHAR(32),\n" +
            "length BIGINT,\n" +
            "identifier VARCHAR(16)\n" +
            ");";

    @DatabaseField(generatedId = true, columnName = "id")
    private int id;

    // TODO MAX LENGTH
    @DatabaseField private String title; // VARCHAR(100)
    @DatabaseField private String author; // VARCHAR(32)
    @DatabaseField private long length;
    @DatabaseField private String identifier; // VARCHAR(16)
    // identifier is the YouTube key of the track

    public Track(int id, String title, String author, long length, String identifier) {
        this(title, author, length, identifier);
        this.id = id;
    }

    public Track(String title, String author, long length, String identifier) {
        this.title = title.length() > 64 ? title.substring(0,64) : title;
        this.author = author.length() > 32 ? author.substring(0,32) : author;
        this.length = length;
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

    public long getLength() {
        return length;
    }

    public Track setLength(long length) {
        this.length = length;
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Track setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

}