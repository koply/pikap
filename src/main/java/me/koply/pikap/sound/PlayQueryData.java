package me.koply.pikap.sound;

public class PlayQueryData {

    public final String order;
    public final boolean isUrl, isPlaylist, playNow, isMusic;
    public final long creationTime;

    public PlayQueryData(String order, boolean isUrl, boolean isPlaylist, boolean playNow, boolean music) {
        this.order = order;
        this.isUrl = isUrl;
        this.isPlaylist = isPlaylist;
        this.playNow = playNow;
        this.isMusic = music;
        this.creationTime = System.currentTimeMillis();
    }
}
