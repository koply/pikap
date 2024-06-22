package me.koply.pikap.commands;

import com.github.tomaslanger.chalk.Ansi;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.Constants;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.cli.command.CLICommand;
import me.koply.pikap.api.cli.command.Command;
import me.koply.pikap.api.cli.command.CommandEvent;
import me.koply.pikap.database.model.FavouriteTrack;
import me.koply.pikap.database.model.Track;
import me.koply.pikap.sound.PlayQueryData;
import me.koply.pikap.util.Util;

import java.util.List;

import static me.koply.pikap.Main.SOUND_MANAGER;

public class FavouriteCommands implements CLICommand {

    private static final Ansi.Color YELLOW = Ansi.Color.YELLOW;
    private static final Ansi.Color BLUE = Ansi.Color.BLUE;

    @Command(usages = {"favs", "favourites", "favorites", "favoriler", "favoris"},
            desc = "Lists the favourite songs.")
    public void favs(CommandEvent e) {
        if (Main.getRepository() == null) {
            Console.println("Database is not found. You can enable database from config.yml");
            return;
        }
        List<FavouriteTrack> favs = Main.getRepository().queryAllFavorites();
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (FavouriteTrack fav : favs) {
            sb.append(YELLOW.getStart()).append("[").append(i++).append("] ").append(YELLOW.getEnd());
            sb.append(BLUE.getStart()).append(fav.getTrack().getTitle()).append(" - ").append(Util.formatMilliSecond(fav.getTrack().getDuration()));
            sb.append(BLUE.getEnd());

            if (i-1 != favs.size()) sb.append("\n");
        }

        Console.println(sb.toString());
    }

    @Command(usages = {"fav", "favourite", "favorite", "favori"},
            desc = "Adds the current song to favourites.")
    public void fav(CommandEvent e) {
        if (Main.getRepository() == null) {
            Console.println("Database is not found. You can enable database from config.yml");
            return;
        }
        AudioTrack track = SOUND_MANAGER.getPlayingTrack();
        if (track == null) {
            println("Doesn't playing anything to fav.");
            return;
        }

        Track dbtrack = Main.getRepository().queryTrackByIdentifier(track.getIdentifier());
        FavouriteTrack favouriteTrack = Main.getRepository().queryFavouriteByTrackId(dbtrack.getId());
        if (favouriteTrack == null) {
            favouriteTrack = new FavouriteTrack(dbtrack);
            Main.getRepository().createFavoriteIfNotExists(favouriteTrack);
            println("Added to favs");
        } else {
            Main.getRepository().deleteFavorite(favouriteTrack);
            println("Removed from favs");
        }
    }

    @Command(usages = {"favr", "favremove", "favdel", "fr"},
            desc = "Removes the selected fav track from favs.",
            usageMsg = "Usage: fr/favr/favdel <index>", sendWithDesc = true)
    public boolean favr(CommandEvent e) {
        if (Main.getRepository() == null) {
            Console.println("Database is not found. You can enable database from config.yml");
            return false;
        }

        if (e.getArgs().length == 1) {
            return true;
        }

        List<FavouriteTrack> favs = Main.getRepository().queryAllFavorites();

        Integer selection = Util.parseInt(e.getArgs()[1]);
        if (selection == null || selection < 0 || selection >= favs.size()) {
            return true;
        }

        FavouriteTrack favouriteTrack = favs.get(selection);

        Main.getRepository().deleteFavorite(favouriteTrack);
        Console.println("The favourite track removed from the favs.");
        return false;
    }

    @Command(usages = {"pf", "playfav", "favplay"},
            desc = "Plays the selected fav track.",
            usageMsg = "Usage: pf/playfav/favplay <index>", sendWithDesc = true)
    public boolean playFav(CommandEvent e) {
        if (Main.getRepository() == null) {
            Console.println("Database is not found. You can enable database from config.yml");
            return false;
        }

        if (e.getArgs().length == 1) {
            return true;
        }

        List<FavouriteTrack> favs = Main.getRepository().queryAllFavorites();

        Integer selection = Util.parseInt(e.getArgs()[1]);
        if (selection == null || --selection < 0 || selection >= favs.size()) {
            return true;
        }

        FavouriteTrack favouriteTrack = favs.get(selection);
        Track track = favouriteTrack.getTrack();

        String url = Constants.YT_URL_PREFIX + track.getIdentifier();
        PlayQueryData data = new PlayQueryData(url, true, false, false, false);
        data.setFromPf(true);
        data.setKnownName(track.getTitle());
        SOUND_MANAGER.playTrack(data);
        return false;
    }

}
