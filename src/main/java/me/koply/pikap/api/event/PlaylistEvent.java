package me.koply.pikap.api.event;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;

public class PlaylistEvent extends AudioEvent {

    public final AudioPlaylist playlist;
    public final long duration;
    public final boolean firstTrackStarted;

    public PlaylistEvent(AudioPlaylist playlist, long duration, boolean firstTrackStarted) {
        this.playlist = playlist;
        this.duration = duration;
        this.firstTrackStarted = firstTrackStarted;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }
}