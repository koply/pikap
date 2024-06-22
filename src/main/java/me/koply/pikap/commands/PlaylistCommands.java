package me.koply.pikap.commands;

import com.github.tomaslanger.chalk.Ansi;
import com.github.tomaslanger.chalk.Chalk;
import me.koply.pikap.Constants;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.cli.command.CLICommand;
import me.koply.pikap.api.cli.command.Command;
import me.koply.pikap.api.cli.command.CommandEvent;
import me.koply.pikap.database.model.Playlist;
import me.koply.pikap.database.model.Track;
import me.koply.pikap.sound.PlayQueryData;
import me.koply.pikap.util.OutputPager;
import me.koply.pikap.util.Util;

import java.util.List;

import static me.koply.pikap.Main.SOUND_MANAGER;

public class PlaylistCommands implements CLICommand {

    private static final Ansi.Color YELLOW = Ansi.Color.YELLOW;
    private static final Ansi.Color BLUE = Ansi.Color.BLUE;

    @Command(usages = {"pl", "playlist"},
            desc = "Lists and plays saved playlists.",
            usageMsg = "Usage: pl | pl <index>", sendWithDesc = true)
    public boolean pl(CommandEvent e) {
        if (Main.getRepository() == null) {
            Console.println("Database is not found. You can enable database from config.yml");
            return false;
        }
        List<Playlist> playlists = Main.getRepository().queryAllPlaylists();
        if (e.getArgs().length == 1) {
            if (playlists.isEmpty()) {
                Console.println("No saved playlists found.");
                return false;
            }
            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (Playlist playlist : playlists) {
                sb.append(YELLOW.getStart()).append("[").append(i++).append("]: ").append(YELLOW.getEnd())
                        .append(BLUE.getStart()).append(playlist.getName())
                        .append(" - (x").append(playlist.getTrackIds().length).append(") ")
                        .append(Util.formatMilliSecond(playlist.getTotalDuration())).append(BLUE.getEnd());

                if (i-1 != playlists.size()) sb.append("\n");
            }

            if (i > Main.CONFIG.getQueuePager()) {
                OutputPager pager = OutputPager.splitAndGenerate(sb.toString(), Main.CONFIG.getQueuePager());
                pager.execute("[PLAYLISTS] Enter page number: ");
            } else {
                Console.println(sb.toString());
            }
        } else {
            Integer selection = Util.parseInt(e.getArgs()[1]);
            if (selection == null) {
                return true;
            }
            Playlist selectedPlaylist = playlists.get(selection-1);
            if (selectedPlaylist == null) {
                return true;
            }
            Console.println("Selected playlist: " + Chalk.on(selectedPlaylist.getName()).blue());
            String playlistYoutubeIdentifier = selectedPlaylist.getYoutubeIdentifier();
            if (playlistYoutubeIdentifier == null) {
                Console.println("Playlist isn't from YouTube.");
                return false;
            }

            int[] ids = selectedPlaylist.getTrackIds();
            if (ids == null || ids.length == 0) {
                Console.println("Playlist members not found?");
                return false;
            }
            Track firstTrack = Main.getRepository().queryTrackById(ids[0]);
            String url = Constants.YT_URL_PREFIX + firstTrack.getIdentifier() + "&list=" + playlistYoutubeIdentifier + "&index=1";
            PlayQueryData data = new PlayQueryData(url, false, true, false, false);
            data.setFromPl(true);
            data.setPlName(selectedPlaylist.getName());
            SOUND_MANAGER.playTrack(data);
        }
        return false;
    }

    @Command(usages = {"plr", "pr"},
            desc = "Removes the selected saved playlist.",
            usageMsg = "Usage: pr/plr <index>", sendWithDesc = true)
    public boolean plr(CommandEvent e) {
        if (Main.getRepository() == null) {
            Console.println("Database is not found. You can enable database from config.yml");
            return false;
        }

        if (e.getArgs().length == 1) {
            return true;
        }
        List<Playlist> playlists = Main.getRepository().queryAllPlaylists();

        Integer selection = Util.parseInt(e.getArgs()[1]);
        if (selection == null) {
            return true;
        }
        Playlist selectedPlaylist = playlists.get(selection-1);
        if (selectedPlaylist == null) {
            return true;
        }
        Main.getRepository().deletePlaylist(selectedPlaylist);
        Console.println("Removed playlist: " + selectedPlaylist.getName());
        return false;
    }

}
