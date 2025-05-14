package me.koply.pikap.sound;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class PikapPipeline extends AudioEventAdapter implements Runnable {

    public static final int AUDIO_BUFFER_SIZE = StandardAudioDataFormats.COMMON_PCM_S16_BE.maximumChunkSize();

    private final PikapAudioPlayerManager audioPlayerManager;
    private final AudioPlayer audioPlayer;

    private AudioInputStream stream;
    private SourceDataLine line;

    private final byte[] buffer = new byte[AUDIO_BUFFER_SIZE];
    private final BlockingQueue<PipelineCommand> commandQueue = new LinkedBlockingQueue<>();

    private final AtomicBoolean isInitialized = new AtomicBoolean(false);

    private boolean paused = false;
    private boolean running = false;

    private PikapPipeline(PikapAudioPlayerManager audioPlayerManager, AudioPlayer audioPlayer) {
        this.audioPlayerManager = audioPlayerManager;
        this.audioPlayer = audioPlayer;

        audioPlayer.addListener(this);
    }

    private static PikapPipeline instance;

    @Contract("null, _ -> fail; _, null -> fail")
    public static PikapPipeline of(PikapAudioPlayerManager audioPlayerManager, AudioPlayer audioPlayer) {
        if (audioPlayerManager == null) throw new IllegalArgumentException("AudioPlayerManager cannot be null.");
        if (audioPlayer == null) throw new IllegalArgumentException("AudioPlayer cannot be null.");
        return instance != null ? instance : (instance = new PikapPipeline(audioPlayerManager, audioPlayer));
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        submitCommand(PipelineCommand.PAUSE);
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        submitCommand(PipelineCommand.RESUME);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (!endReason.mayStartNext) {
            submitCommand(PipelineCommand.PAUSE);
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        submitCommand(PipelineCommand.RESUME);
    }

    public void shutdown() {
        submitCommand(PipelineCommand.SHUTDOWN);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void submitCommand(PipelineCommand command) {
        commandQueue.offer(command);
    }

    public void start() throws LineUnavailableException {
        if (!isInitialized.compareAndSet(false, true)) {
            log.info("prepareAndStart already called. Skipping re-initialization.");
            return;
        }

        AudioDataFormat format = this.audioPlayerManager.getConfiguration().getOutputFormat();
        AudioInputStream innerStream = AudioPlayerInputStream.createStream(this.audioPlayer, format, 10000L, false);
        SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, innerStream.getFormat());
        SourceDataLine innerLine = (SourceDataLine) AudioSystem.getLine(info);
        innerLine.open(innerStream.getFormat());
        innerLine.start();

        stream = innerStream;
        line = innerLine;

        running = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        int chunkSize;

        try {
            while (running) {
                PipelineCommand command = commandQueue.poll();
                if (command != null) {
                    handlePipelineCommand(command);
                }

                if (paused) {
                    Thread.sleep(50);
                    continue;
                }

                chunkSize = stream.read(buffer);
                if (chunkSize >= 0) {
                    line.write(buffer, 0, chunkSize);
                }
            }

            stream.close();
            line.close();

            isInitialized.set(false);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void handlePipelineCommand(PipelineCommand command) {
        switch (command) {
            case PAUSE:
                paused = true;
                break;
            case RESUME:
                paused = false;
                break;
            case SHUTDOWN:
                running = false;
                break;
        }
    }
}