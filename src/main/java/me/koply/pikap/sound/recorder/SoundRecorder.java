package me.koply.pikap.sound.recorder;

import me.koply.pikap.Main;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class SoundRecorder {

    private final File recordingsFolder;
    private final Path recordingsPath;
    private final AsynchronousFileChannel fileChannel;

    public SoundRecorder() {
        if (!Main.CONFIG.recordCheck()) {
            throw new RuntimeException("The config entry of 'record' is setted to false. Cannot initialize the SoundRecorder.");
        }

        String recFolder = Main.CONFIG.get("recfolder");
        recordingsFolder = recFolder != null ? new File(recFolder) : new File("recs/");

        this.recordingsPath = recordingsFolder.toPath();

        AsynchronousFileChannel temp = null;
        try {
            temp = AsynchronousFileChannel.open(recordingsPath, StandardOpenOption.WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.fileChannel = temp;
    }

    public void save(byte[] buffer) {
        //fileChannel.write(ByteBuffer.wrap(buffer), );
    }
}