package me.koply.pikap.session;

import me.koply.pikap.api.event.*;

public class DataListener extends EventListenerAdapter {

    private final SessionData store;

    public DataListener(SessionData store) {
        this.store = store;
    }

    @Override
    public void onPlay(PlayEvent e) {
        store.getPlayingNow().setPlayingTrack(e.track);
        System.out.println("onplay q: " + e.isAddedToQueue + " r: " + e.reason);
    }

    @Override
    public void onTrackEnd(TrackEndEvent e) {
        if (e.endTrack != null) {
            store.pushPastTrack(e.endTrack);
        }
    }

    @Override
    public void onNextTrack(NextTrackEvent e) {
        if (e.pastTrack != null) {
            store.pushPastTrack(e.pastTrack);
        }
    }

    @Override
    public void onPlaylist(PlaylistEvent e) {
        System.out.println("onplaylist d: " + e.duration);
    }
}