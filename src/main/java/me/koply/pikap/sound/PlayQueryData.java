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
        this.fromPl = false;
        this.plName = null;
    }

    private boolean fromPl;
    private String plName;

    public boolean isFromPl() {
        return fromPl;
    }

    public void setFromPl(boolean fromPl) {
        this.fromPl = fromPl;
    }

    public String getPlName() {
        return plName;
    }

    public void setPlName(String plName) {
        this.plName = plName;
    }
}
