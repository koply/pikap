package me.koply.pikap.session;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.event.EventManager;

import java.util.ArrayList;
import java.util.List;

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
    private final List<AudioTrack> previousTracks = new ArrayList<>(10);

    private Playing playingNow;
    // TODO

    public List<AudioTrack> getPreviousTracks() {
        return previousTracks;
    }

    public AudioTrack getLastTrack() {
        if (previousTracks.isEmpty()) {
            return null;
        } else {
            return previousTracks.get(previousTracks.size()-1);
        }
    }

    public void addTrack(AudioTrack track) {
        AudioTrack last = getLastTrack();
        if (last != null && last.getInfo() != track.getInfo()) {
            previousTracks.add(track);
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