package me.koply.pikap.session;

import me.koply.pikap.api.event.*;

public class SessionDataListener extends EventListenerAdapter {

    private final SessionData store;

    public SessionDataListener(SessionData store) {
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
    public void onTrackEnd(TrackEndEvent e) {
        store.addTrack(e.endTrack);
        store.setCurrentState(State.IDLE);
        store.getPlayingNow().setPlayingTrack(null);
    }

    @Override
    public void onNextTrack(NextTrackEvent e) {
        store.addTrack(e.pastTrack);
        store.getPlayingNow().setPlayingTrack(e.nextTrack);
        store.setCurrentState(State.PLAYING_TRACK);
    }

    @Override
    public void onPlaylist(PlaylistEvent e) {
        store.getPlayingNow().setPlayingTrack(e.playlist.getTracks().get(0));
        store.getPlayingNow().setPlayingPlaylist(e.playlist);
    }
}