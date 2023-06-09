package me.koply.pikap.sound;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.model.PlayQueryData;

import java.util.concurrent.TimeUnit;

import static com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats.COMMON_PCM_S16_BE;

public class SoundManager {

    private static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private static final AudioPlayer player = playerManager.createPlayer();
    private static final EqualizerFactory equalizerFactory = new EqualizerFactory();
    private static final PipelineController PIPELINE = new PipelineController(playerManager, player);
    private static final TrackManager scheduler = new TrackManager(player, PIPELINE);
    private static final SearchResultHandler handler = new SearchResultHandler(scheduler);

    public SoundManager() {
        // we don't need a bunch of these sources
        // AudioSourceManagers.registerRemoteSources(playerManager);
        // just YouTube for now
        playerManager.registerSourceManager(new YoutubeAudioSourceManager(true));

        playerManager.getConfiguration().setOutputFormat(COMMON_PCM_S16_BE);
        playerManager.setPlayerCleanupThreshold(TimeUnit.HOURS.toMillis(24));
        player.addListener(scheduler);
        setVolume(75);
    }

    private static final float[] BASS_BOOST = { 0.2f, 0.15f, 0.1f, 0.05f, 0.0f,
            -0.05f, -0.1f, -0.1f, -0.1f, -0.1f,
            -0.1f, -0.1f, -0.1f, -0.1f, -0.1f };

    public static void shutdown() {
        playerManager.shutdown();
        PIPELINE.shutdownThread();
        Console.info("Sound system closed.");
    }

    // -------------- PUBLIC API -----------------
    public void playTrack(PlayQueryData data) {
        Console.info("Searching... \"" + data.order + "\"");

        handler.setQueryData(data);
        String order = data.isUrl ? data.order : "ytsearch:"+data.order;
        playerManager.loadItem(order, handler);
    }

    public AudioTrack getPlayingTrack() {
        return player.getPlayingTrack();
    }

    public void nextTrack(int number) {
        scheduler.nextTrack(number);
    }

    public void setVolume(int x) {
        player.setVolume(x);
    }

    public int getVolume() {
        return player.getVolume();
    }

    public void activeEqualizer() {
        player.setFilterFactory(equalizerFactory);
    }

    public void disableEqualizer() {
        player.setFilterFactory(null);
    }

    public void increaseBassBoost(float diff) {
        for (int i = 0; i < BASS_BOOST.length; i++) {
            equalizerFactory.setGain(i, BASS_BOOST[i] + diff);
        }
    }

    public void decreaseBassBoost(float diff) {
        for (int i = 0; i < BASS_BOOST.length; i++) {
            equalizerFactory.setGain(i, -BASS_BOOST[i] + diff);
        }
    }

    private boolean paused = false;
    public boolean isPaused() { return paused; }

    public void playPauseButton() {
        if (paused) resume();
        else pause();
    }

    public void pause() {
        player.setPaused(true);
        PIPELINE.pauseOutputLine();
        paused = true;
        Console.info("Paused.");
    }

    public void resume() {
        player.setPaused(false);
        PIPELINE.resumeOutputLine();
        paused = false;
        Console.info("Resumed.");
    }

    public void stop() {
        player.stopTrack();
        Console.info("Track stopped.");
    }

}