package me.koply.pikap.test;

import me.koply.pikap.api.event.EventListenerAdapter;
import me.koply.pikap.api.event.NextTrackEvent;
import me.koply.pikap.api.event.PlayEvent;
import me.koply.pikap.api.event.TrackEndEvent;

public class AudioEventDebugger extends EventListenerAdapter {

    @Override
    public void onPlay(PlayEvent e) {
        //Console.info("AudioEventDebugger#onPlayEvent");
    }

    @Override
    public void onTrackEnd(TrackEndEvent e) {
        //Console.info("AudioEventDebugger#onTrackEnd");
    }

    @Override
    public void onNextTrack(NextTrackEvent e) {
        //Console.info("AudioEventDebugger#onNextTrack");
    }
}