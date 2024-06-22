package me.koply.pikap.sound;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class PikapQueue {

    // TODO
    private final LinkedBlockingQueue<AudioTrack> tracks;
    private final List<QueueNode> leftTrackNumbersFromPlaylists;

    public PikapQueue() {
        tracks = new LinkedBlockingQueue<>();
        leftTrackNumbersFromPlaylists = Collections.synchronizedList(new ArrayList<>());
    }

    public void addQueue(AudioTrack track) {
        tracks.offer(track);
    }

}
