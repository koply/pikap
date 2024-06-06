package me.koply.pikap.session;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

public class Playing {

    /**
     * Currently playing track.
     */
    private AudioTrack playingTrack;

    /**
     * If null, pikap doesn't playing a playlist right now
     */
    private AudioPlaylist playingPlaylist;

    public AudioTrackInfo getTrackInfo() {
        return playingTrack.getInfo();
    }

    public AudioTrack getPlayingTrack() {
        return playingTrack;
    }

    public Playing setPlayingTrack(AudioTrack playingTrack) {
        this.playingTrack = playingTrack;
        return this;
    }

    public AudioPlaylist getPlayingPlaylist() {
        return playingPlaylist;
    }

    public Playing setPlayingPlaylist(AudioPlaylist playingPlaylist) {
        this.playingPlaylist = playingPlaylist;
        return this;
    }
}