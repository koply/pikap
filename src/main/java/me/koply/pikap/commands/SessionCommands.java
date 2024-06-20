package me.koply.pikap.commands;

import com.github.tomaslanger.chalk.Ansi;
import com.github.tomaslanger.chalk.Chalk;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.cli.command.CLICommand;
import me.koply.pikap.api.cli.command.Command;
import me.koply.pikap.api.cli.command.CommandEvent;
import me.koply.pikap.util.TrackUtil;

import java.util.List;

import static me.koply.pikap.Main.SESSION;

public class SessionCommands implements CLICommand {

    @Command(usages = {"past", "session", "showsession"}, desc = "Shows the previous tracks of the current session.")
    public void som(CommandEvent e) {
        StringBuilder sb = new StringBuilder();
        sb.append(Chalk.on("Current state: ").yellow().toString())
                .append(Chalk.on(SESSION.getCurrentState().toString()).blue().toString())
                .append(Chalk.on("\nPlaying now: ").yellow().toString()).append(Ansi.Color.BLUE.getStart());
        if (SESSION.getPlayingNow().getPlayingTrack() != null) {
            sb.append(TrackUtil.trackToString(SESSION.getPlayingNow().getPlayingTrack()));
        } else {
            sb.append("-");
        }
        sb.append(Ansi.Color.BLUE.getEnd()).append("\n");

        List<AudioTrack> tracks = SESSION.getPreviousTracks();

        for (int i = 0; i<tracks.size(); i++) {
            if (tracks.get(i) == null) continue;
            if (tracks.get(i).getInfo() == null) continue;

            sb.append(Ansi.Color.YELLOW.getStart()).append("[").append(i+1).append("] ").append(Ansi.Color.YELLOW.getEnd())
                    .append(Ansi.Color.BLUE.getStart())
                    .append(TrackUtil.trackToString(tracks.get(i))).append(Ansi.Color.BLUE.getEnd()).append("\n");
        }

        Console.prln(sb.toString());
    }

}