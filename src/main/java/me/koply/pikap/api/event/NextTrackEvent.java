package me.koply.pikap.api.event;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.sound.SoundManager;

public class NextTrackEvent extends AudioEvent {

    public final AudioTrack pastTrack;
    public final AudioTrack nextTrack;
    public final Reason reason;

    public NextTrackEvent(SoundManager soundManager, AudioTrack pastTrack, AudioTrack nextTrack, Reason reason) {
        super(soundManager);
        this.pastTrack = pastTrack;
        this.nextTrack = nextTrack;
        this.reason = reason;
    }

    public enum Reason {
        TRACK_END, NEXT
    }
}