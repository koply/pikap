package me.koply.pikap.api.event;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.sound.SoundManager;

public class ReplayEvent extends AudioEvent {

    public final AudioTrack track;

    public ReplayEvent(SoundManager soundManager, AudioTrack track) {
        super(soundManager);
        this.track = track;
    }

}
