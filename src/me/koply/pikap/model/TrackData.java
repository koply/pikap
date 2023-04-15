package me.koply.pikap.model;

public class TrackData {
    public final String title;
    public final String author;
    public final long length;
    public final String uri;
    public final long lastPlayed;

    public TrackData(String title, String author, long length, String uri, long lastPlayed) {
        this.title = title;
        this.author = author;
        this.length = length;
        this.uri = uri;
        this.lastPlayed = lastPlayed;
    }

}