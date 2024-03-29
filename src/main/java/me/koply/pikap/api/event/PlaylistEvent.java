package me.koply.pikap.api.event;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import me.koply.pikap.sound.SoundManager;

public class PlaylistEvent extends AudioEvent {

    public final AudioPlaylist playlist;
    public final long duration;
    public final boolean firstTrackStarted;

    public PlaylistEvent(SoundManager soundManager, AudioPlaylist playlist, long duration, boolean firstTrackStarted) {
        super(soundManager);
        this.playlist = playlist;
        this.duration = duration;
        this.firstTrackStarted = firstTrackStarted;
    }
}