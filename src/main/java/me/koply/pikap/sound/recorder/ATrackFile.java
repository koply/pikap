package me.koply.pikap.sound.recorder;

import java.io.File;

public abstract class ATrackFile {

    private final File trackFile;

    public ATrackFile(File trackFile) {
        this.trackFile = trackFile;
    }
}