package me.koply.pikap.commands;

import com.github.tomaslanger.chalk.Chalk;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.cli.command.CLICommand;
import me.koply.pikap.api.cli.command.Command;
import me.koply.pikap.api.cli.command.CommandEvent;
import me.koply.pikap.api.cli.command.OnlyInstance;
import me.koply.pikap.model.PlayQueryData;
import me.koply.pikap.util.Util;

import static me.koply.pikap.Main.soundManager;

@OnlyInstance
public class TrackControlCommands implements CLICommand {

    private static TrackControlCommands instance;
    public static TrackControlCommands getInstance() {
        return instance != null ? instance : (instance = new TrackControlCommands());
    }

    @Command(usages = {"play", "p", "pn", "pp", "search"},
            desc = "You can play songs. 'pn' for play first result. 'pp' for play-playlist.")
    public void play(CommandEvent e) {
        String order = e.getPureCommand().substring(e.getArgs()[0].length()).trim();
        boolean now = e.getArgs()[0].endsWith("n");
        boolean playlist = e.getArgs()[0].length() == 2 && e.getArgs()[0].endsWith("p");

        if (order.length() == 0) {
            println("Please enter a query.");
            return;
        }
        boolean isUrl = Util.isUrl(order);
        soundManager.playTrack(new PlayQueryData(order, isUrl, playlist, now));

        try {
            synchronized (instance) {
                instance.wait();
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Command(usages = "pause", desc = "Pause's the song.")
    public void pause(CommandEvent e) {
        soundManager.pause();
    }

    @Command(usages = "resume", desc = "Resume's the song.")
    public void resume(CommandEvent e) {
        soundManager.resume();
    }

    @Command(usages = "stop", desc = "Stop's the song.")
    public void stop(CommandEvent e) {
        soundManager.stop();
    }

    @Command(usages = {"next", "skip", "n"}, desc = "Switches to next track. You can enter number.")
    public void next(CommandEvent e) {
        int number = 1;
        if (e.getArgs().length == 2) {
            String arg = e.getArgs()[1];
            Integer temp;
            number = (temp = Util.parseInt(arg)) == null ? 1 : temp;
        }
        soundManager.nextTrack(number);
    }

    @Command(usages = {"volume", "vol", "volum", "sound"}, desc = "Sets the volume of the song.")
    public void volume(CommandEvent e) {
        if (e.getArgs().length < 2) {
           println("Current volume: " + soundManager.getVolume());
           return;
        }
        Integer volume = Util.parseInt(e.getArgs()[1]);
        if (volume == null) {
            println("Invalid entry. Current volume: " + soundManager.getVolume());
        } else {
            volume = volume > 100 ? 100 : volume < 0 ? 0 : volume;
            soundManager.setVolume(volume);
            println("New volume: " + volume);
        }
    }

    @Command(usages = "now", desc = "Shows info of the playing track.")
    public void nowPlaying(CommandEvent e) {
        if (soundManager.getPlayingTrack() == null) {
            println("Silence...");
        } else {
            Console.prln(Util.getNowPlayingBox(soundManager));
        }
    }

    @Command(usages = "seek", desc = "Seeks the playing track with given seconds.")
    public void seek(CommandEvent e) {
        AudioTrack track = soundManager.getPlayingTrack();
        if (track == null) {
            Console.println("Nothing is playing right now.");
            return;
        }

        if (e.getArgs().length != 1) {
            String valueStr = e.getArgs()[1];
            boolean reverse = valueStr.startsWith("-");
            Integer value = reverse ? Util.parseInt(valueStr.substring(1)) : Util.parseInt(valueStr);
            if (value == null) {
                Console.println("Example usage: " + Chalk.on("seek 15").green() + " (If you enter a negative number, it'll seek backwards.)");
                return;
            }

            long seekValue = value * 1000;
            long position = track.getPosition();
            long seekableZone = reverse ? position : track.getDuration() - position;

            if (seekValue < seekableZone) {
                long newPosition = reverse ? position - seekValue : position + seekValue;
                track.setPosition(newPosition);
                Console.prln(Util.getNowPlayingBox(soundManager));
            } else {
                Console.println("The entered number is greater than track duration.");
            }
            return;
        }
        Console.prefixln("Example usage: " + Chalk.on("seek 15").green() + Chalk.on(" (If you enter a negative number, it'll seek backwards.)").yellow());
    }

    @Command(usages = "eq", desc = "Equalizer configuration.")
    public void eq(CommandEvent e) {
        soundManager.activeEqualizer();
        // 0 1     2 3
        // eq bass + 1
        if (e.getArgs().length <= 3) {
            println("Example usage: eq bass + 0.3");
        } else if (e.getArgs()[1].equals("bass")) {
            Float diff = Util.parseFloat(e.getArgs()[3]);
            if (diff == null) {
                println("Invalid diff.");
                return;
            }
            if (e.getArgs()[2].equals("+")) {
                soundManager.increaseBassBoost(diff);
            } else if (e.getArgs()[2].equals("-")) {
                soundManager.decreaseBassBoost(diff);
            }
        }
    }

}