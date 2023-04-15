package me.koply.pikap.model;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class PastTrack {

    public long id;
    public final AudioTrack track;
    public final long listeningMillis;

    public PastTrack(AudioTrack track, long listeningMillis) {
        this.track = track;
        this.listeningMillis = listeningMillis;
    }

    public PastTrack(long id, AudioTrack track, long listeningMillis) {
        this.track = track;
        this.listeningMillis = listeningMillis;
        this.id = id;
    }

}