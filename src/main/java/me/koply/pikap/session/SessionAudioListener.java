package me.koply.pikap.session;

import me.koply.pikap.api.event.*;
import me.koply.pikap.event.AudioListenerAdapter;
import me.koply.pikap.event.EventHandler;

public class SessionAudioListener extends AudioListenerAdapter {

    private final SessionData store;

    public SessionAudioListener(SessionData store) {
        this.store = store;
    }

    @EventHandler
    @Override
    public void onPlay(PlayEvent e) {
        if (!e.isAddedToQueue) {
            store.getPlayingNow().setPlayingTrack(e.track);
            store.setCurrentState(State.PLAYING_TRACK);
        }
    }

    @EventHandler
    @Override
    public void onPause(PauseEvent e) {
        if (store.getCurrentState() == State.PLAYING_TRACK) {
            store.setCurrentState(State.PAUSED_TRACK);
        } else if (store.getCurrentState() == State.PLAYING_PLAYLIST) {
            store.setCurrentState(State.PAUSED_PLAYLIST);
        }
    }

    @EventHandler
    @Override
    public void onResume(ResumeEvent e) {
        if (store.getCurrentState() == State.PAUSED_TRACK) {
            store.setCurrentState(State.PLAYING_TRACK);
        } else if (store.getCurrentState() == State.PAUSED_PLAYLIST) {
            store.setCurrentState(State.PLAYING_PLAYLIST);
        }
    }

    @EventHandler
    @Override
    public void onTrackEnd(TrackEndEvent e) {
        store.addTrack(e.endTrack);
        store.setCurrentState(State.IDLE);
        store.getPlayingNow().setPlayingTrack(null);
    }

    @EventHandler
    @Override
    public void onNextTrack(NextTrackEvent e) {
        if (e.pastTrack != null) store.addTrack(e.pastTrack);
        store.getPlayingNow().setPlayingTrack(e.nextTrack);
        store.setCurrentState(State.PLAYING_TRACK);
    }

    @EventHandler
    @Override
    public void onPlaylist(PlaylistEvent e) {
        store.getPlayingNow().setPlayingTrack(e.playlist.getTracks().get(0));
        store.getPlayingNow().setPlayingPlaylist(e.playlist);
        store.setCurrentState(State.PLAYING_PLAYLIST);
    }
}