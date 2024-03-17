package me.koply.pikap.test;

import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.event.*;

public class AudioEventDebugger extends EventListenerAdapter {

    @Override
    public void onPlay(PlayEvent e) {
        Console.log("AudioEventDebugger#onPlayEvent");
    }

    @Override
    public void onTrackEnd(TrackEndEvent e) {
        Console.log("AudioEventDebugger#onTrackEnd");
    }

    @Override
    public void onNextTrack(NextTrackEvent e) {
        Console.log("AudioEventDebugger#onNextTrack");
    }

    @Override
    public void onPlaylist(PlaylistEvent e) {
        Console.log("AudioEventDebugger#onPlaylist");
    }

    @Override
    public void onPause(PauseEvent e) {
        Console.log("AudioEventDebugger#onPause");
    }

    @Override
    public void onResume(ResumeEvent e) {
        Console.log("AudioEventDebugger#onResume");
    }
}