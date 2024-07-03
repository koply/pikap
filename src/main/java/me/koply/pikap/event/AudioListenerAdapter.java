package me.koply.pikap.event;

import me.koply.pikap.api.event.*;
import me.koply.pikap.util.architechture.Observer;

@SuppressWarnings("unused")
public abstract class AudioListenerAdapter implements Observer{

    public void onPlay(PlayEvent e) { }
    public void onTrackEnd(TrackEndEvent e) { }
    public void onNextTrack(NextTrackEvent e) { }
    public void onPlaylist(PlaylistEvent e) { }
    public void onPause(PauseEvent e) { }
    public void onResume(ResumeEvent e) { }
    public void onReplay(ReplayEvent e) { }

    @Override
    public void update() {}
}