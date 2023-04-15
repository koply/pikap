package me.koply.pikap.model;

public class PlayQueryData {

    public final String order;
    public final boolean isUrl, isPlaylist, playNow;
    public final long creationTime;

    public PlayQueryData(String order, boolean isUrl, boolean isPlaylist, boolean playNow) {
        this.order = order;
        this.isUrl = isUrl;
        this.isPlaylist = isPlaylist;
        this.playNow = playNow;
        this.creationTime = System.currentTimeMillis();
    }
}
