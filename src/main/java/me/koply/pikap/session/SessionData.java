package me.koply.pikap.session;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.event.EventManager;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicLong;

public class SessionData {

    private final SessionDataListener sessionDataListener;
    public SessionData() {
        sessionDataListener = new SessionDataListener(this);
    }

    public void registerListener() {
        EventManager.registerListener(sessionDataListener);
    }

    private State currentState = State.IDLE; // getter, setter
    public static final AtomicLong bufferCycles = new AtomicLong();

    /**
     * Last played 10 tracks.
     */
    private final Stack<AudioTrack> previousTracks = new Stack<>();

    private Playing playingNow;
    // TODO

    public Stack<AudioTrack> getPreviousTracks() {
        return previousTracks;
    }

    public AudioTrack popLastTrack() {
        if (previousTracks.isEmpty()) {
            return null;
        } else {
            return previousTracks.pop();
        }
    }

    public void addTrack(AudioTrack track) {
        if (previousTracks.isEmpty()) {
            previousTracks.push(track);
        } else if (previousTracks.get(0).getInfo() != track.getInfo()) {
            previousTracks.push(track);
        }
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
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