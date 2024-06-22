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
        this.knownName = null;
    }

    // from playlist command
    private boolean fromPl;

    // playlist name if its from playlist command
    // favourited track's name if its from playfav command
    private String knownName;

    // from playfav command
    private boolean fromPf;


    public boolean isFromPl() {
        return fromPl;
    }

    public void setFromPl(boolean fromPl) {
        this.fromPl = fromPl;
    }

    public boolean isFromPf() {
        return fromPf;
    }

    public void setFromPf(boolean fromPf) {
        this.fromPf = fromPf;
    }

    public String getKnownName() {
        return knownName;
    }

    public void setKnownName(String knownName) {
        this.knownName = knownName;
    }
}
