package me.koply.pikap.sound.recorder;

import me.koply.pikap.Main;
import me.koply.pikap.api.event.EventListenerAdapter;
import me.koply.pikap.api.event.TrackEndEvent;
import me.koply.pikap.session.Playing;

import java.util.ArrayList;
import java.util.List;

public class SoundRecorder extends EventListenerAdapter{

    private final List<TrackRecorder> tracks = new ArrayList<>();

    public SoundRecorder() {

    }

    public void save(byte[] buffer) {
        Playing playing = Main.SESSION.getPlayingNow();

    }

    @Override
    public void onTrackEnd(TrackEndEvent e) {
        // şarkı bitti track objesini serialize et
    }
}