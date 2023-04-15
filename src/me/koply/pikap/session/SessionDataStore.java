package me.koply.pikap.session;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.event.EventManager;
import me.koply.pikap.util.FixedStack;

public class SessionDataStore {

    public enum State {
        IDLE, PLAYING_TRACK, PAUSED_TRACK, PLAYING_PLAYLIST, PAUSED_PLAYLIST
    }

    public static class Playing {
        /**
         * Currently playing track.
         */
        private AudioTrack playingTrack;

        /**
         * If null, pikap doesn't playing a playlist right now
         */
        private AudioPlaylist playingPlaylist;
    }

    /**
     * Last played 10 tracks.
     */
    private final FixedStack<AudioTrack> previousTracks = new FixedStack<>(10);

    private final SessionDataListener sessionDataListener;
    private State currentState = State.IDLE; // getter, setter

    public SessionDataStore() {
        sessionDataListener = new SessionDataListener(this);
        EventManager.registerListener(sessionDataListener);
    }


    // TODO


    public AudioTrack popPastTrack() {
        return previousTracks.pop();
    }

    private void pushPastTrack(AudioTrack track) {
        previousTracks.push(track);
    }

    public State getCurrentState() {
        return currentState;
    }

    public SessionDataStore setCurrentState(State currentState) {
        this.currentState = currentState;
        return this;
    }
}