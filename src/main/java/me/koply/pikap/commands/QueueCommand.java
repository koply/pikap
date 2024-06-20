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
import me.koply.pikap.util.Util;

import static me.koply.pikap.Main.SESSION;

public class QueueCommand implements CLICommand {

    private static final Ansi.Color YELLOW = Ansi.Color.YELLOW;
    private static final Ansi.Color BLUE = Ansi.Color.BLUE;

    @Command(usages = "queue", desc = "Displays track queue.")
    public void queue(CommandEvent e) {
        StringBuilder sb = new StringBuilder();

        int i = 1;
        for (AudioTrack track : Main.SOUND_MANAGER.getQueue()) {
            AudioTrackInfo trackInfo = track.getInfo();
            sb.append(YELLOW.getStart()).append("[").append(i++).append("] ").append(YELLOW.getEnd());
            sb.append(BLUE.getStart()).append(trackInfo.title).append(" - ").append(Util.formatMilliSecond(trackInfo.length));
            sb.append(BLUE.getEnd()).append("\n");
        }

        Console.println(sb.toString());
    }

    @Command(usages = "last", desc = "Queue's the last track.")
    public void last(CommandEvent e) {
        // TODO - last with numbers for played previously
        AudioTrack last = SESSION.popLastTrack().makeClone();
        SoundManager.getTrackManager().play(last, PlayEvent.Reason.PLAY_LAST);
    }

}