package me.koply.pikap.api.event;

import me.koply.pikap.sound.SoundManager;

public abstract class AudioEvent {

    public final SoundManager soundManager;
    protected AudioEvent(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }
}