package me.koply.pikap.sound;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.event.PauseEvent;
import me.koply.pikap.api.event.ResumeEvent;
import me.koply.pikap.api.event.TrackEndEvent;
import me.koply.pikap.event.EventManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats.COMMON_PCM_S16_BE;

public class SoundManager {

    private static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private static final AudioPlayer player = playerManager.createPlayer();
    private static final EqualizerFactory equalizerFactory = new EqualizerFactory();
    private static final PipelineController pipeline = new PipelineController(playerManager, player);
    private static final TrackManager trackManager = new TrackManager(player, pipeline);
    private static final SearchResultHandler handler = new SearchResultHandler(trackManager);

    public static TrackManager getTrackManager() {
        return trackManager;
    }

    public SoundManager() {
    }

    public void initialize() {
        // we don't need a bunch of these sources
        // AudioSourceManagers.registerRemoteSources(playerManager);
        // just YouTube for now
        playerManager.registerSourceManager(new YoutubeAudioSourceManager(true));

        playerManager.getConfiguration().setOutputFormat(COMMON_PCM_S16_BE);
        playerManager.setPlayerCleanupThreshold(TimeUnit.HOURS.toMillis(24));
        player.addListener(trackManager);
        setVolume(75);
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return trackManager.queue;
    }

    private static final float[] BASS_BOOST = { 0.2f, 0.15f, 0.1f, 0.05f, 0.0f,
            -0.05f, -0.1f, -0.1f, -0.1f, -0.1f,
            -0.1f, -0.1f, -0.1f, -0.1f, -0.1f };

    public static void shutdown() {
        playerManager.shutdown();
        pipeline.shutdownThread();
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

    public void nextTrack(int count) {
        trackManager.nextTrack(count);
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

    public boolean isPaused() {
        return player.isPaused();
    }

    public void pause() {
        player.setPaused(true);
        pipeline.pauseOutputLine();
        EventManager.pushEvent(
                new PauseEvent(Main.SOUND_MANAGER, player.getPlayingTrack()));

        Console.info("Paused.");
    }

    public void resume() {
        player.setPaused(false);
        pipeline.resumeOutputLine();
        EventManager.pushEvent(
                new ResumeEvent(Main.SOUND_MANAGER, player.getPlayingTrack()));

        Console.info("Resumed.");
    }

    public void stop() {
        player.stopTrack();
        pipeline.shutdownThread(); // be careful
        EventManager.pushEvent(
                new TrackEndEvent(Main.SOUND_MANAGER, player.getPlayingTrack(), AudioTrackEndReason.STOPPED));

        Console.info("Track stopped.");
    }

}