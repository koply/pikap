package me.koply.pikap.sound;

import com.github.tomaslanger.chalk.Ansi;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.event.PlayEvent;
import me.koply.pikap.api.event.PlaylistEvent;
import me.koply.pikap.commands.TrackControlCommands;
import me.koply.pikap.event.EventPublisher;
import me.koply.pikap.util.Util;

import java.util.List;

public class SearchResultHandler implements AudioLoadResultHandler {

    private static final Ansi.Color YELLOW = Ansi.Color.YELLOW;
    private static final Ansi.Color BLUE = Ansi.Color.BLUE;

    private final QueueScheduler scheduler;
    private final EventPublisher eventPublisher = EventPublisher.getInstance();
    public SearchResultHandler(QueueScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        if (scheduler.getQueryData().isUrl) {
            scheduler.play(audioTrack, PlayEvent.Reason.URL);
        } else {
            Console.info("TrackLoaded: " + audioTrack.getInfo().title);
        }

        notifyCommandHandler();
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        List<AudioTrack> playlist = audioPlaylist.getTracks();
        if (scheduler.getQueryData().isPlaylist) { // pp command
            // eventless because playlists have own event
            boolean started = scheduler.eventlessPlay(playlist.get(0));
            long duration = playlist.get(0).getInfo().length;
            duration += scheduler.addQueuePlaylist(audioPlaylist);

            Console.prefixln("The playlist queued with " + playlist.size() + " tracks. Total time: " + Util.formatMilliSecond(duration));
            eventPublisher.publishEvent(new PlaylistEvent(audioPlaylist, duration, started));

        } else if (scheduler.getQueryData().playNow) { // pn command
            scheduler.play(playlist.get(0), PlayEvent.Reason.PLAY_NOW);

        } else { // play, p, search command
            Console.prefixln("Search result:");

            StringBuilder sb = new StringBuilder();

            int searchLimitor = Main.CONFIG.getSearchLimitor();
            int limitor = searchLimitor == -1 ? playlist.size() : Math.min(playlist.size(), searchLimitor);

            for (int i = 0; i<limitor; ++i) {
                AudioTrackInfo trackInfo = playlist.get(i).getInfo();

                sb.append(YELLOW.getStart()).append("[").append(i+1).append("] ").append(YELLOW.getEnd());
                sb.append(BLUE.getStart()).append(trackInfo.title).append(" - ").append(Util.formatMilliSecond(trackInfo.length));
                sb.append(BLUE.getEnd());

                if (i != playlist.size()-1) sb.append("\n");
            }

            Console.println(sb.toString());
            int choice = Util.readInteger("Enter your choice.", 1, limitor);
            if (choice != 0) scheduler.play(audioPlaylist.getTracks().get(choice - 1), PlayEvent.Reason.SEARCH);
            else Console.prefixln("Order canceled.");
        }

        notifyCommandHandler();
    }

    @Override
    public void noMatches() {
        Console.info("No results about '" + scheduler.getQueryData().order + "'");

        notifyCommandHandler();
    }

    @Override
    public void loadFailed(FriendlyException e) {
        Console.info("Something happened. " + e.getMessage());

        notifyCommandHandler();
    }

    private void notifyCommandHandler() {
        synchronized (TrackControlCommands.getInstance()) {
            TrackControlCommands.getInstance().notify();
        }
    }
}