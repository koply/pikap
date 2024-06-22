package me.koply.pikap.commands;

import com.github.tomaslanger.chalk.Ansi;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.cli.command.CLICommand;
import me.koply.pikap.api.cli.command.Command;
import me.koply.pikap.api.cli.command.CommandEvent;
import me.koply.pikap.api.event.PlayEvent;
import me.koply.pikap.sound.SoundManager;
import me.koply.pikap.util.OutputPager;
import me.koply.pikap.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static me.koply.pikap.Main.SESSION;

public class QueueCommand implements CLICommand {

    private static final Ansi.Color YELLOW = Ansi.Color.YELLOW;
    private static final Ansi.Color BLUE = Ansi.Color.BLUE;

    @Command(usages = "queue", desc = "Displays track queue.")
    public void queue(CommandEvent e) {
        StringBuilder sb = new StringBuilder();
        BlockingQueue<AudioTrack> queue = Main.SOUND_MANAGER.getQueue();
        int queuePager = Main.CONFIG.getQueuePager();
        int i = 1;
        if (queue.size() < queuePager+2) { // single page
            for (AudioTrack track : queue) {
                AudioTrackInfo trackInfo = track.getInfo();
                sb.append(YELLOW.getStart()).append("[").append(i++).append("] ").append(YELLOW.getEnd());
                sb.append(BLUE.getStart()).append(trackInfo.title).append(" - ").append(Util.formatMilliSecond(trackInfo.length));
                sb.append(BLUE.getEnd()).append("\n");
            }
            Console.println(sb.toString());
        } else { // paging
            List<String> pages = new ArrayList<>();
            for (AudioTrack track : queue) {
                AudioTrackInfo trackInfo = track.getInfo();
                sb.append(YELLOW.getStart()).append("[").append(i++).append("] ").append(YELLOW.getEnd());
                sb.append(BLUE.getStart()).append(trackInfo.title).append(" - ").append(Util.formatMilliSecond(trackInfo.length));
                sb.append(BLUE.getEnd());
                if ((i-1) % queuePager == 0) {
                    pages.add(sb.toString());
                    sb.setLength(0);
                } else if (i-1 != queue.size()) sb.append("\n");
            }
            if (sb.length() > 0) pages.add(sb.toString());

            OutputPager pager = new OutputPager(pages, 0);
            pager.execute("[QUEUE] Enter page number: ");
        }
    }

    @Command(usages = "last", desc = "Queue's the last track.")
    public void last(CommandEvent e) {
        // TODO - last with numbers for played previously
        AudioTrack last = SESSION.popLastTrack().makeClone();
        SoundManager.getQueueScheduler().play(last, PlayEvent.Reason.PLAY_LAST);
    }

}