package me.koply.pikap.api.event;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class PlayEvent extends AudioEvent {

    /**
     * The track just started playing.
     */
    public final AudioTrack track;
    public final boolean isAddedToQueue;
    public final Reason reason;

    public PlayEvent(AudioTrack track, boolean isAddedToQueue, Reason reason) {
        this.track = track;
        this.isAddedToQueue = isAddedToQueue;
        this.reason = reason;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    public enum Reason {
        // Next and playlist has their own events.
        SEARCH, PLAY_NOW, URL, PLAY_LAST
    }
}