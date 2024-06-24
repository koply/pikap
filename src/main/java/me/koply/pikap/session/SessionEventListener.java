package me.koply.pikap.session;

import me.koply.pikap.api.event.*;

public class SessionEventListener extends EventListenerAdapter {

    private final SessionData store;

    public SessionEventListener(SessionData store) {
        this.store = store;
    }

    @Override
    public void onPlay(PlayEvent e) {
        if (!e.isAddedToQueue) {
            store.getPlayingNow().setPlayingTrack(e.track);
            store.setCurrentState(State.PLAYING_TRACK);
        }
    }

    @Override
    public void onPause(PauseEvent e) {
        if (store.getCurrentState() == State.PLAYING_TRACK) {
            store.setCurrentState(State.PAUSED_TRACK);
        } else if (store.getCurrentState() == State.PLAYING_PLAYLIST) {
            store.setCurrentState(State.PAUSED_PLAYLIST);
        }
    }

    @Override
    public void onResume(ResumeEvent e) {
        if (store.getCurrentState() == State.PAUSED_TRACK) {
            store.setCurrentState(State.PLAYING_TRACK);
        } else if (store.getCurrentState() == State.PAUSED_PLAYLIST) {
            store.setCurrentState(State.PLAYING_PLAYLIST);
        }
    }

    @Override
    public void onTrackEnd(TrackEndEvent e) {
        store.addTrack(e.endTrack);
        store.setCurrentState(State.IDLE);
        store.getPlayingNow().setPlayingTrack(null);
    }

    @Override
    public void onNextTrack(NextTrackEvent e) {
        if (e.pastTrack != null) store.addTrack(e.pastTrack);
        store.getPlayingNow().setPlayingTrack(e.nextTrack);
        store.setCurrentState(State.PLAYING_TRACK);
    }

    @Override
    public void onPlaylist(PlaylistEvent e) {
        store.getPlayingNow().setPlayingTrack(e.playlist.getTracks().get(0));
        store.getPlayingNow().setPlayingPlaylist(e.playlist);
        store.setCurrentState(State.PLAYING_PLAYLIST);
    }
}