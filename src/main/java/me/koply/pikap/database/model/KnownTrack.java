package me.koply.pikap.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

@DatabaseTable(tableName = "knowntracks")
public class KnownTrack {

    // unused
    private static final String CREATE_TRACKS_TABLE = "CREATE TABLE [IF NOT EXISTS] knowntracks (\n" +
            "id INTEGER PRIMARY KEY,\n" +
            "title VARCHAR(64),\n" +
            "author VARCHAR(32),\n" +
            "length BIGINT,\n" +
            "identifier VARCHAR(16)\n" +
            ");";

    @DatabaseField(id = true)
    private int id;

    // TODO MAX LENGTH
    @DatabaseField private String title; // VARCHAR(100)
    @DatabaseField private String author; // VARCHAR(32)
    @DatabaseField private long length;
    @DatabaseField private String identifier; // VARCHAR(16)
    // identifier is the YouTube key of the track

    public KnownTrack(int id, String title, String author, long length, String identifier) {
        this(title, author, length, identifier);
        this.id = id;
    }

    public KnownTrack(String title, String author, long length, String identifier) {
        this.title = title.length() > 64 ? title.substring(0,64) : title;
        this.author = author.length() > 32 ? author.substring(0,32) : author;
        this.length = length;
        this.identifier = identifier;
    }

    public KnownTrack(AudioTrackInfo info) {
        this(info.title, info.author, info.length, info.identifier);
    }

    public KnownTrack() { }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public KnownTrack setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public KnownTrack setAuthor(String author) {
        this.author = author;
        return this;
    }

    public long getLength() {
        return length;
    }

    public KnownTrack setLength(long length) {
        this.length = length;
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public KnownTrack setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

}