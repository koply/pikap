package me.koply.pikap.session;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.event.EventManager;
import me.koply.pikap.util.FixedStack;

public class SessionData {

    private final DataListener sessionDataListener;
    public SessionData() {
        sessionDataListener = new DataListener(this);
    }

    public void registerListener() {
        EventManager.registerListener(sessionDataListener);
    }

    private State currentState = State.IDLE; // getter, setter

    /**
     * Last played 10 tracks.
     */
    private final FixedStack<AudioTrack> previousTracks = new FixedStack<>(10);

    private Playing playingNow;
    // TODO

    public FixedStack<AudioTrack> getPreviousTracks() {
        return previousTracks;
    }

    public AudioTrack popPastTrack() {
        return previousTracks.pop();
    }

    public void pushPastTrack(AudioTrack track) {
        previousTracks.push(track);
    }

    public State getCurrentState() {
        return currentState;
    }

    public SessionData setCurrentState(State currentState) {
        this.currentState = currentState;
        return this;
    }

    public Playing getPlayingNow() {
        if (playingNow == null) playingNow = new Playing();
        return playingNow;
    }

    public SessionData setPlayingNow(Playing playingNow) {
        this.playingNow = playingNow;
        return this;
    }
}