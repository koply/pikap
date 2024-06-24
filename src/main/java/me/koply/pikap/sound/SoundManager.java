package me.koply.pikap.sound;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.clients.AndroidLite;
import dev.lavalink.youtube.clients.Music;
import dev.lavalink.youtube.clients.MusicWithThumbnail;
import dev.lavalink.youtube.clients.Web;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.event.PauseEvent;
import me.koply.pikap.api.event.ResumeEvent;
import me.koply.pikap.api.event.TrackEndEvent;
import me.koply.pikap.event.EventPublisher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats.COMMON_PCM_S16_BE;

public class SoundManager {

    private SoundManager() {
    }

    private static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private static final AudioPlayer player = playerManager.createPlayer();
    private static final EqualizerFactory equalizerFactory = new EqualizerFactory();
    private static final PipelineController pipeline = new PipelineController(playerManager, player);
    private static final AtomicBoolean replay = new AtomicBoolean(false);
    private static final QueueScheduler QUEUE_SCHEDULER = new QueueScheduler(player, pipeline, replay);
    private static final SearchResultHandler handler = new SearchResultHandler(QUEUE_SCHEDULER);
    private final EventPublisher eventPublisher = EventPublisher.getInstance();

    public static QueueScheduler getQueueScheduler() {
        return QUEUE_SCHEDULER;
    }

    public static String getOrder() {
        PlayQueryData data = QUEUE_SCHEDULER.getQueryData();
        return data == null ? null : data.order;
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return QUEUE_SCHEDULER.queue;
    }

    private static SoundManager instance = null;

    public static SoundManager getInstance() {
        if(instance!= null) return instance;

        instance = new SoundManager();

        playerManager.registerSourceManager(new YoutubeAudioSourceManager(true, new Web(), new Music(), new MusicWithThumbnail(), new AndroidLite()));

        playerManager.getConfiguration().setOutputFormat(COMMON_PCM_S16_BE);
        playerManager.setPlayerCleanupThreshold(TimeUnit.HOURS.toMillis(24));
        player.addListener(QUEUE_SCHEDULER);
        player.setVolume(75);

        return instance;
    }

    public static void shutdown() {
        playerManager.shutdown();
        pipeline.shutdownThread();
        Console.info("Sound system closed.");
    }

    // -------------- PUBLIC API -----------------
    public void playTrack(PlayQueryData data) {
        if (data.isFromPl()) {
            Console.info("Retrieving playlist: \"" + data.getKnownName() + "\"");
        } else if (data.isFromPf()) {
            Console.info("Retrieving favourited track: \"" + data.getKnownName() + "\"");
        } else {
            Console.info("Searching... \"" + data.order + "\"");
        }

        QUEUE_SCHEDULER.setQueryData(data);
        String order = data.isUrl || data.isFromPl() ? data.order : (data.isMusic ? "ytmsearch:"+data.order : "ytsearch:"+data.order);
        playerManager.loadItem(order, handler);
    }

    public AudioTrack getPlayingTrack() {
        return player.getPlayingTrack();
    }

    public void nextTrack(int count) {
        QUEUE_SCHEDULER.nextTrack(count);
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

    private static final float[] BASS_BOOST = { 0.2f, 0.15f, 0.1f, 0.05f, 0.0f,
            -0.05f, -0.1f, -0.1f, -0.1f, -0.1f,
            -0.1f, -0.1f, -0.1f, -0.1f, -0.1f };

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
        if (!player.isPaused() && player.getPlayingTrack() != null) {

            player.setPaused(true);
            pipeline.pauseOutputLine();

            eventPublisher.publishEvent(
                    new PauseEvent(player.getPlayingTrack()));
            Console.info("Paused.");
        }
    }

    public void resume() {
        if (player.isPaused()) {
            player.setPaused(false);
            pipeline.resumeOutputLine();
            eventPublisher.publishEvent(
                    new ResumeEvent( player.getPlayingTrack()));

            Console.info("Resumed.");
        }
    }

    public boolean getReplay() {
        return replay.get();
    }

    public void setReplay(boolean value) {
        replay.set(value);
    }

    public void stop() {
        AudioTrack lastTrack = player.getPlayingTrack() != null ? player.getPlayingTrack().makeClone() : null;
        if (lastTrack != null) {
            lastTrack.setPosition(player.getPlayingTrack().getPosition());
            eventPublisher.publishEvent(
                    new TrackEndEvent(lastTrack, AudioTrackEndReason.STOPPED));
        }
        player.stopTrack();
        pipeline.shutdownThread(); // be careful
        player.setPaused(false);

        Console.info("Track stopped.");
    }

}