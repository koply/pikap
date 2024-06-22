package me.koply.pikap.api.event;

@SuppressWarnings("unused")
public abstract class EventListenerAdapter {

    public void onPlay(PlayEvent e) { }
    public void onTrackEnd(TrackEndEvent e) { }
    public void onNextTrack(NextTrackEvent e) { }
    public void onPlaylist(PlaylistEvent e) { }
    public void onPause(PauseEvent e) { }
    public void onResume(ResumeEvent e) { }
    public void onReplay(ReplayEvent e) { }

}