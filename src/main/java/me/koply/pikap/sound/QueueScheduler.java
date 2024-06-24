package me.koply.pikap.sound;

import com.github.tomaslanger.chalk.Chalk;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.event.NextTrackEvent;
import me.koply.pikap.api.event.PlayEvent;
import me.koply.pikap.api.event.ReplayEvent;
import me.koply.pikap.api.event.TrackEndEvent;
import me.koply.pikap.event.EventManager;
import me.koply.pikap.util.StringUtil;
import me.koply.pikap.util.TrackUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class QueueScheduler extends AudioEventAdapter {
    
    private final SoundManager soundManager = SoundManager.getInstance();

    private final AudioPlayer player;
    private final PipelineController pipeline;
    public final BlockingQueue<AudioTrack> queue;
    private final AtomicBoolean replay;

    // TODO: Use PikapQueue insted of BlockingQueue
    // TODO: Remember playlist queues.

    private PlayQueryData queryData = null;
    public void setQueryData(PlayQueryData queryData) {
        this.queryData = queryData;
    }
    public PlayQueryData getQueryData() {
        return queryData;
    }

    public QueueScheduler(AudioPlayer player, PipelineController pipeline, AtomicBoolean replay) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.pipeline = pipeline;
        this.replay = replay;
    }

    /**
     * @param track to add queue
     */
    public void addQueue(AudioTrack track) {
        queue.offer(track);
    }

    /**
     * @param playlist to add queue
     * @return total duration of the playlist
     */
    public long addQueuePlaylist(AudioPlaylist playlist) {
        long duration = 0;
        for (int i = 1; i<playlist.getTracks().size(); i++) {
            queue.offer(playlist.getTracks().get(i));
            duration += playlist.getTracks().get(i).getInfo().length;
        }
        return duration;
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
            Console.println(StringUtil.getTrackBoxWithCurrentTime(soundManager));
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
                new PlayEvent(track, !isStarted, reason));
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
        Console.prln(Chalk.on("[ Next: " + skipped + " -→ ] ").green().toString() + suffix);
        Console.println(StringUtil.getTrackBoxWithCurrentTime(soundManager));

        EventManager.pushEvent(
                new NextTrackEvent(lastTrack, poll, NextTrackEvent.Reason.NEXT));
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

        if (replay.get()) {
            AudioTrack replayTrack = track.makeClone();
            EventManager.pushEvent(new ReplayEvent(replayTrack));
            Console.prln(Chalk.on("[ Replay: On ]").green().toString());
            Console.println(StringUtil.getTrackBoxWithCurrentTime(soundManager));
            return;
        }

        if (queue.isEmpty()) {
            EventManager.pushEvent(
                    new TrackEndEvent( track, endReason));
            Console.info("Empty queue...");
            return;
        }

        AudioTrack poll = queue.poll();

        player.startTrack(poll, false);

        Console.prln(Chalk.on("[ -→ ]").green().toString());
        Console.println(StringUtil.getTrackBoxWithCurrentTime(soundManager));

        EventManager.pushEvent(
                new NextTrackEvent(track, poll, NextTrackEvent.Reason.TRACK_END));
     }

}