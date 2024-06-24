package me.koply.pikap.test;

import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.event.*;
import me.koply.pikap.event.AudioListenerAdapter;
import me.koply.pikap.event.EventHandler;

public class AudioAudioDebugger extends AudioListenerAdapter {

    @EventHandler
    @Override
    public void onPlay(PlayEvent e) {
        Console.debugLog("AudioEventDebugger#onPlayEvent");
    }

    @EventHandler
    @Override
    public void onTrackEnd(TrackEndEvent e) {
        Console.debugLog("AudioEventDebugger#onTrackEnd");
    }

    @EventHandler
    @Override
    public void onNextTrack(NextTrackEvent e) {
        Console.debugLog("AudioEventDebugger#onNextTrack");
    }

    @EventHandler
    @Override
    public void onPlaylist(PlaylistEvent e) {
        Console.debugLog("AudioEventDebugger#onPlaylist");
    }

    @EventHandler
    @Override
    public void onPause(PauseEvent e) {
        Console.debugLog("AudioEventDebugger#onPause");
    }

    @EventHandler
    @Override
    public void onResume(ResumeEvent e) {
        Console.debugLog("AudioEventDebugger#onResume");
    }
}