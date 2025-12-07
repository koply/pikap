package me.koply.pikap.sound;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.YoutubeSourceOptions;
import dev.lavalink.youtube.clients.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;
import javax.sound.sampled.LineUnavailableException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Singleton
public class MediaFacade {
    
    private final PikapAudioPlayerManager audioPlayerManager;
    private final AudioPlayer audioPlayer;
    private final PikapPipeline pipeline;

    private final LinkedBlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();

    public MediaFacade() {
        audioPlayerManager = new PikapAudioPlayerManager();

        var options = new YoutubeSourceOptions()
                .setRemoteCipher("https://cipher.kikkia.dev/", "", "me.koply.pikap | 1.0")
                .setAllowSearch(true);

        var youtubeAudioSourceManager = new YoutubeAudioSourceManager(options, new MWeb(), new MWebWithThumbnail(), new Web(), new WebWithThumbnail(), new Ios(), new IosWithThumbnail(), new Music());

        audioPlayerManager.registerSourceManager(youtubeAudioSourceManager);
        audioPlayerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());

        audioPlayerManager.getConfiguration().setOutputFormat(StandardAudioDataFormats.COMMON_PCM_S16_BE);
        audioPlayerManager.setPlayerCleanupThreshold(TimeUnit.HOURS.toMillis(24));

        audioPlayer = audioPlayerManager.constructPlayer();

        // Singleton
        this.pipeline = PikapPipeline.of(audioPlayerManager, audioPlayer);

        try {
            this.pipeline.start();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        pipeline.shutdown();
        audioPlayerManager.shutdown();
    }

    public void registerEventAdapter(AudioEventAdapter adapter) {
        audioPlayer.addListener(adapter);
    }

    public void queryAsync(String query, FunctionalResultHandler handler) {
        AudioReference reference = new AudioReference(query, "");
        audioPlayerManager.loadItem(reference, handler);
    }

    public void queryTrackAsync(String query, Consumer<AudioTrack> consumer) {
        AudioReference reference = new AudioReference(query, "");
        audioPlayerManager.loadItemPikap(reference, (item) -> {
            System.out.println("item geldi");
            switch (item) {
                case AudioTrack track -> {
                    System.out.println("item track");
                    consumer.accept(track);
                }
                case AudioPlaylist playlist -> System.out.println("item playlist");
                case null -> System.out.println("item null knk");
                default -> {
                }
            }
        });
    }

    @Nullable
    public AudioTrack queryTrackSync(String query) {
        AudioReference reference = new AudioReference(query, null);
        var item = audioPlayerManager.loadItemSyncPikap(reference);
        return item instanceof AudioTrack ? (AudioTrack) item : null;

    }

    public void queryPlaylistAsync(String query, Consumer<AudioPlaylist> consumer) {
        AudioReference reference = new AudioReference(query, null);
        audioPlayerManager.loadItemPikap(reference, (item) -> {
            if (item instanceof AudioPlaylist) {
                consumer.accept((AudioPlaylist) item);
            }
        });
    }

    @Nullable
    public AudioPlaylist queryPlaylistSync(String query) {
        AudioReference reference = new AudioReference(query, null);
        var item = audioPlayerManager.loadItemSyncPikap(reference);
        return item instanceof AudioPlaylist ? (AudioPlaylist) item : null;
    }

    public void play(@NotNull AudioTrack track) {
        if (audioPlayer.getPlayingTrack() != null) {
            queue.add(track);
        } else {
            audioPlayer.playTrack(track);
        }
    }

    // TODO, adds to queue all songs. and event listener for next track at end track event
    public void play(@NotNull AudioPlaylist playlist) {
        queue.addAll(playlist.getTracks());

        AudioTrack temp;
        if (audioPlayer.getPlayingTrack() == null && (temp = queue.poll()) != null) {
            play(temp);
        }
    }

    public void stop() {
        audioPlayer.stopTrack();
    }

    public void pause() {
        audioPlayer.setPaused(true);
    }

    public void resume() {
        audioPlayer.setPaused(false);
    }

    public void setVolume(int volume) {
        audioPlayer.setVolume(volume);
    }

    public int getVolume() {
        return audioPlayer.getVolume();
    }

    public Optional<AudioTrack> getCurrentTrack() {
        return Optional.of(audioPlayer.getPlayingTrack());
    }

    public AudioTrack next() {
        return next(1);
    }

    public AudioTrack next(int count) {
        AudioTrack track = null;
        for (int i = 0; i< count; i++) {
            track = queue.poll();
        }

        if (track != null) {
            audioPlayer.playTrack(track);
        }
        return track;
    }

    public Collection<AudioTrack> getQueue() {
        return Collections.unmodifiableCollection(queue);
    }
}