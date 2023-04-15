package me.koply.pikap.api.event;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import me.koply.pikap.sound.SoundManager;

public class PlaylistEvent extends AudioEvent {

    public final AudioPlaylist playlist;
    public final long duration;

    public PlaylistEvent(SoundManager soundManager, AudioPlaylist playlist, long duration) {
        super(soundManager);
        this.playlist = playlist;
        this.duration = duration;
    }
}