package me.koply.pikap.api.event;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class ResumeEvent extends AudioEvent {

    public final AudioTrack track;

    public ResumeEvent(AudioTrack track) {
        this.track = track;
    }
}