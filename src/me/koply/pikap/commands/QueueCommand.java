package me.koply.pikap.commands;

import com.github.tomaslanger.chalk.Ansi;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.cli.command.CLICommand;
import me.koply.pikap.api.cli.command.Command;
import me.koply.pikap.api.cli.command.CommandEvent;
import me.koply.pikap.session.SessionData;
import me.koply.pikap.util.Util;

public class QueueCommand implements CLICommand {


    @Command(usages = "queue", desc = "Displays track queue.")
    public void queue(CommandEvent e) {
        SessionData session = Main.SESSION;

    }

    private static final Ansi.Color YELLOW = Ansi.Color.YELLOW;
    private static final Ansi.Color BLUE = Ansi.Color.BLUE;

    @Command(usages = "last", desc = "Play again last track.")
    public void last(CommandEvent e) {
        SessionData session = Main.SESSION;

        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (AudioTrack previousTrack : session.getPreviousTracks()) {
            AudioTrackInfo trackInfo = previousTrack.getInfo();
            sb.append(YELLOW.getStart()).append("[").append(i).append("] ").append(YELLOW.getEnd());
            sb.append(BLUE.getStart()).append(trackInfo.title).append(" - ").append(Util.formatMilliSecond(trackInfo.length));
            sb.append(BLUE.getEnd());
            i++;
        }

        Console.println(sb.toString());
    }

}