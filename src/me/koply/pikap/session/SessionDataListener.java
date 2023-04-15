package me.koply.pikap.session;

import me.koply.pikap.api.event.*;

public class SessionDataListener extends EventListenerAdapter {

    private final SessionDataStore store;

    public SessionDataListener(SessionDataStore store) {
        this.store = store;
    }

    @Override
    public void onPlay(PlayEvent e) {
        super.onPlay(e);
    }

    @Override
    public void onTrackEnd(TrackEndEvent e) {
        super.onTrackEnd(e);
    }

    @Override
    public void onNextTrack(NextTrackEvent e) {
        super.onNextTrack(e);
    }

    @Override
    public void onPlaylist(PlaylistEvent e) {
        super.onPlaylist(e);
    }
}