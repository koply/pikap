package me.koply.pikap.commands;

import com.github.tomaslanger.chalk.Ansi;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.cli.command.CLICommand;
import me.koply.pikap.api.cli.command.Command;
import me.koply.pikap.api.cli.command.CommandEvent;
import me.koply.pikap.database.model.FavouriteTrack;
import me.koply.pikap.database.model.Track;
import me.koply.pikap.util.Util;

import java.util.List;

import static me.koply.pikap.Main.SOUND_MANAGER;

public class FavouriteCommand implements CLICommand {

    @Command(usages = {"fav", "favourite"}, desc = "Adds the current song to favourites.")
    public void fav(CommandEvent e) {
        AudioTrack track = SOUND_MANAGER.getPlayingTrack();
        if (track == null) {
            println("Doesn't playing anything to fav.");
            return;
        }

        Track dbtrack = Main.getRepository().queryTrackByIdentifier(track.getIdentifier());
        FavouriteTrack favouriteTrack = new FavouriteTrack(dbtrack);

        Main.getRepository().createFavoriteIfNotExists(favouriteTrack);
        println("Added to favs");
    }

    private static final Ansi.Color YELLOW = Ansi.Color.YELLOW;
    private static final Ansi.Color BLUE = Ansi.Color.BLUE;

    @Command(usages = {"favs", "favourites"}, desc = "Lists the favourite songs.")
    public void favs(CommandEvent e) {
        List<FavouriteTrack> favs = Main.getRepository().queryAllFavorites();
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (FavouriteTrack fav : favs) {
            sb.append(YELLOW.getStart()).append("[").append(i++).append("] ").append(YELLOW.getEnd());
            sb.append(BLUE.getStart()).append(fav.getTrack().getTitle()).append(" - ").append(Util.formatMilliSecond(fav.getTrack().getDuration()));
            sb.append(BLUE.getEnd()).append("\n");
        }

        Console.println(sb.toString());
    }
}
