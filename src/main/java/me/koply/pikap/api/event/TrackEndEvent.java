package me.koply.pikap.api.event;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class TrackEndEvent extends AudioEvent {

    public final AudioTrack endTrack;
    public final AudioTrackEndReason reason;

    public TrackEndEvent(AudioTrack track, AudioTrackEndReason reason) {
        this.endTrack = track;
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
}