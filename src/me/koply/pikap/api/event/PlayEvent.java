package me.koply.pikap.api.event;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.sound.SoundManager;

public class PlayEvent extends AudioEvent {

    /**
     * The track just started playing.
     */
    public final AudioTrack track;
    public final boolean isAddedToQueue;
    public final Reason reason;

    public PlayEvent(SoundManager soundManager, AudioTrack track, boolean isAddedToQueue, Reason reason) {
        super(soundManager);
        this.track = track;
        this.isAddedToQueue = isAddedToQueue;
        this.reason = reason;
    }

    public enum Reason {
        // Next and playlist has their own events.
        SEARCH, PLAY_NOW, URL
    }
}