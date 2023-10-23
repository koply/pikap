package me.koply.pikap.sound;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import me.koply.pikap.Constants;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.sound.recorder.SoundRecorder;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.io.InterruptedIOException;

public class PipelineController implements Runnable {

    private final AudioPlayerManager playerManager;
    private final AudioPlayer player;

    private final SoundRecorder recorder;
    private final boolean record;

    public PipelineController(AudioPlayerManager playerManager, AudioPlayer player) {
        this.playerManager = playerManager;
        this.player = player;

        record = Main.CONFIG.recordCheck();
        recorder = record ? new SoundRecorder() : null;
    }

    private boolean isPause = false;
    public void pauseOutputLine() {
        isPause = true;
    }

    private AudioInputStream stream;
    private SourceDataLine line;

    private Thread workerThread = new Thread(this);

    public void shutdownThread() {
        try {
            player.destroy();
            if (stream != null) stream.close();
            if (workerThread == null) return;
            if (!workerThread.isInterrupted() && workerThread.isAlive()) workerThread.interrupt();
            workerThread = null;
        } catch (InterruptedIOException ignored) {
            Console.log("InterruptedIO at pipeline.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void resumeOutputLine() {
        isPause = false;
        workerThread = null;
        workerThread = new Thread(this);
        workerThread.start();
    }

    public void prepareAndRun() {
        try {
            AudioDataFormat format = this.playerManager.getConfiguration().getOutputFormat();
            AudioInputStream innerStream = AudioPlayerInputStream.createStream(this.player, format, 10000L, false);
            SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, innerStream.getFormat());
            SourceDataLine innerLine = (SourceDataLine) AudioSystem.getLine(info);
            innerLine.open(innerStream.getFormat());
            innerLine.start();

            stream = innerStream;
            line = innerLine;
            if (isPause) {
                workerThread = new Thread(this);
                isPause = false;
            }
            if (workerThread != null && !workerThread.isAlive()) {
                workerThread.start();
            }
        } catch (Exception ex) {
            if (Main.CONFIG.getOrDefault("debug", "false").equals("true")) {
                ex.printStackTrace();
            } else {
                Console.println("An exception occur while preparing AudioOutput.");
            }
        }
    }

    private final byte[] buffer = new byte[Constants.AUDIO_BUFFER_SIZE];

    @Override
    public void run() {
        int chunkSize;

        try {
            while ((chunkSize = stream.read(buffer)) >= 0) {
                line.write(buffer, 0, chunkSize);
                if (record) recorder.save(buffer);

                if (isPause) break;
            }
        } catch (InterruptedIOException ignored) {
            Console.log("InterruptedIO at pipeline.");
        } catch (IOException ex) {
            if (Main.CONFIG.isDebug()) {
                Console.println("[DEBUG] Exception stacktrace:");
                ex.printStackTrace();
            } else {
                Console.println("[E] -> AudioOutput stream.");
            }
        }
    }
}