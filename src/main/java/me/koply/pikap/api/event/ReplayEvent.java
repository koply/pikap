package me.koply.pikap.api.event;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class ReplayEvent extends AudioEvent {

    public final AudioTrack track;

    public ReplayEvent(AudioTrack track) {
        this.track = track;
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
