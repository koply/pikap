package me.koply.pikap.sound;

import com.github.tomaslanger.chalk.Chalk;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.event.NextTrackEvent;
import me.koply.pikap.api.event.PlayEvent;
import me.koply.pikap.api.event.TrackEndEvent;
import me.koply.pikap.event.EventManager;
import me.koply.pikap.util.StringUtil;
import me.koply.pikap.util.TrackUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackManager extends AudioEventAdapter {

    private final AudioPlayer player;
    private final PipelineController pipeline;
    public final BlockingQueue<AudioTrack> queue;

    public TrackManager(AudioPlayer player, PipelineController pipeline) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.pipeline = pipeline;
    }

    /**
     * @param track to add queue
     */
    public void addQueue(AudioTrack track) {
        queue.offer(track);
    }

    /**
     * Pure play method. Just plays and notifies user.
     *
     * @param track to play
     * @return if the track is started, returns true
     */
    private boolean play(AudioTrack track) {
        boolean isStarted = player.startTrack(track, true);
        if (!isStarted) {
            queue.offer(track);
            Console.prefixln("Added to queue. (" + Chalk.on(TrackUtil.trackToString(track)).green() + ")");
        } else {
            pipeline.prepareAndRun();
            Console.println(StringUtil.getTrackBoxWithCurrentTime(Main.SOUND_MANAGER));
        }
        return isStarted;
    }

    /**
     * Plays the track but it wouldn't push event.
     * @param track to play
     * @return if the track is started, returns true
     */
    public boolean eventlessPlay(AudioTrack track) {
        return play(track);
    }

    /**
     * Plays and pushes PlayEvent with given reason
     *
     * @param track to play
     * @param reason of the event
     */
    public void play(AudioTrack track, PlayEvent.Reason reason) {
        boolean isStarted = play(track);
        EventManager.pushEvent(
                new PlayEvent(Main.SOUND_MANAGER, track, !isStarted, reason));
    }

    /**
     * This method runs only triggered by user with next/skip command.
     *
     * @param number switch to next track count
     */
    public void nextTrack(int number) {
        if (queue.isEmpty()) return;
        AudioTrack poll = null;
        int skipped = 0;
        for (int i = 0; i<number; i++) {
            poll = queue.poll();
            if (queue.isEmpty()) break;
            skipped += 1;
        }

        AudioTrack lastTrack = player.getPlayingTrack();
        player.startTrack(poll, false);

        String suffix = number != skipped ? Chalk.on("(" + number +")").red().toString() : "";
        Console.prln(Chalk.on("[ " + skipped + " -→ ] ").green().toString() + suffix);
        Console.println(StringUtil.getTrackBoxWithCurrentTime(Main.SOUND_MANAGER));

        EventManager.pushEvent(
                new NextTrackEvent(Main.SOUND_MANAGER, lastTrack, poll, NextTrackEvent.Reason.NEXT));
    }

    /**
     * This method runs by Lavaplayer.
     *
     * @param player Audio player
     * @param track Audio track that ended
     * @param endReason The reason why the track stopped playing
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (!endReason.mayStartNext) {
            return;
        }

        if (queue.isEmpty()) {
            EventManager.pushEvent(
                    new TrackEndEvent(Main.SOUND_MANAGER, track, endReason));
            Console.info("Empty queue...");
            return;
        }

        AudioTrack poll = queue.poll();

        player.startTrack(poll, false);

        Console.prln(Chalk.on("[ -→ ]").green().toString());
        Console.println(StringUtil.getTrackBoxWithCurrentTime(Main.SOUND_MANAGER));

        EventManager.pushEvent(
                new NextTrackEvent(Main.SOUND_MANAGER, track, poll, NextTrackEvent.Reason.TRACK_END));
     }

}