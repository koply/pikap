package me.koply.pikap.sound.recorder;

import me.koply.pikap.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.StandardOpenOption;

public class RecFileData {

    private final File dataFile;
    private AsynchronousFileChannel fileChannel;
    private long cursor;

    public RecFileData(File dataFile) throws IOException {
        this.dataFile = dataFile;
        this.fileChannel = AsynchronousFileChannel.open(dataFile.toPath(), StandardOpenOption.WRITE);
    }

    public void postData(byte[] data) {
        postData(data, cursor);
    }

    public void postData(byte[] data, long cursor) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        fileChannel.write(buffer, cursor);
        this.cursor = cursor + Constants.AUDIO_BUFFER_SIZE;
    }
}