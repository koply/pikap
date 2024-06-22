package me.koply.pikap.test;

import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.event.*;

public class AudioEventDebugger extends EventListenerAdapter {

    @Override
    public void onPlay(PlayEvent e) {
        Console.debugLog("AudioEventDebugger#onPlayEvent");
    }

    @Override
    public void onTrackEnd(TrackEndEvent e) {
        Console.debugLog("AudioEventDebugger#onTrackEnd");
    }

    @Override
    public void onNextTrack(NextTrackEvent e) {
        Console.debugLog("AudioEventDebugger#onNextTrack");
    }

    @Override
    public void onPlaylist(PlaylistEvent e) {
        Console.debugLog("AudioEventDebugger#onPlaylist");
    }

    @Override
    public void onPause(PauseEvent e) {
        Console.debugLog("AudioEventDebugger#onPause");
    }

    @Override
    public void onResume(ResumeEvent e) {
        Console.debugLog("AudioEventDebugger#onResume");
    }
}