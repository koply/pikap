package me.koply.pikap.api.event;

import me.koply.pikap.event.Event;
import me.koply.pikap.sound.SoundManager;

public abstract class AudioEvent implements Event {

    private final SoundManager soundManager = SoundManager.getInstance();

    @Override
    public String toString() {
        return this.getClass().getName();
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }
}