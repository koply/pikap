package me.koply.pikap.commands;

import com.github.tomaslanger.chalk.Chalk;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.cli.command.CLICommand;
import me.koply.pikap.api.cli.command.Command;
import me.koply.pikap.api.cli.command.CommandEvent;
import me.koply.pikap.api.cli.command.OnlyInstance;
import me.koply.pikap.sound.PlayQueryData;
import me.koply.pikap.sound.SoundManager;
import me.koply.pikap.util.*;

import java.util.Locale;

import static me.koply.pikap.Main.CONFIG;

@OnlyInstance
public class TrackControlCommands implements CLICommand {

    private final SoundManager soundManager = SoundManager.getInstance();

    private static TrackControlCommands instance;
    public static TrackControlCommands getInstance() {
        return instance != null ? instance : (instance = new TrackControlCommands());
    }

    @Command(usages = {"play", "p", "pn", "pp", "pm", "search"},
            desc = "You can play songs. 'pn' for play first result. 'pp' for play-playlist. 'pm' for YoutubeMusic search.",
            usageMsg = "Usages: p/play/search <query> | pn <query | pp <playlist url>",
            sendWithDesc = true)

    public boolean play(CommandEvent e) {
        String order = e.getPureCommand().substring(e.getArgs()[0].length()).trim();

        if (order.isEmpty()) {
            return true;
        }

        boolean now = e.getArgs()[0].endsWith("n");
        boolean music = e.getArgs()[0].endsWith("m");
        boolean playlist = e.getArgs()[0].equalsIgnoreCase("pp");
        boolean isUrl = Util.isUrl(order);

        if (playlist && !(RegexUtil.isYoutubeMatch(order) && order.toLowerCase(Locale.ROOT).contains("list="))) {
            Console.println("You didn't enter a valid YouTube link. Still make a search? All results will be added to the queue.");
            boolean x = Util.readBoolean();
            if (!x) {
                Console.println("Aborted.");
                return false;
            }
        }

        PlayQueryData data = new PlayQueryData(order, isUrl, playlist, now, music);
        data.setFromPl(false);
        soundManager.playTrack(data);

        try {
            synchronized (instance) {
                instance.wait();
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return false;
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

    @Command(usages = {"replay", "r", "repeat"}, desc = "Replay mode.")
    public void replay(CommandEvent e) {
        if (e.getArgs().length == 1) {
            soundManager.setReplay(!soundManager.getReplay());
        } else if (StringUtil.anyEqualsIgnoreCase(e.getArgs()[1], "on", "active")) {
            soundManager.setReplay(true);
        } else if (StringUtil.anyEqualsIgnoreCase(e.getArgs()[1], "off", "deactive")) {
            soundManager.setReplay(false);
        }

        Console.info("Replay mode: " + soundManager.getReplay());
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
            int maxVolume = Util.parseIntOrDefault(CONFIG.get("maximum_volume"), 100);
            volume = volume > maxVolume ? maxVolume : volume < 0 ? 0 : volume;
            soundManager.setVolume(volume);
            println("New volume: " + volume);
        }
    }

    @Command(usages = "now", desc = "Shows info of the playing track.")
    public void nowPlaying(CommandEvent e) {
        if (soundManager.getPlayingTrack() == null) {
            println("Silence...");
            return;
        }
        Console.prln(TrackBox.build(soundManager));
        Console.println("[ Replay: " + soundManager.getReplay() + " - Remaining Queue: " + soundManager.getQueue().size() + " ]");

        if (e.getArgs().length > 1) {
            Console.prln(TrackUtil.trackToStringDetailed(soundManager.getPlayingTrack().getInfo()));
        }
    }

    @Command(usages = "seek", desc = "Seeks the playing track with given seconds.")
    public void seek(CommandEvent e) {
        if (e.getArgs().length == 1) {
            Console.prefixln("Example usage: " + Chalk.on("seek 15").green() + Chalk.on(" (If you enter a negative number, it'll seek backwards.)").yellow());
            return;
        }

        AudioTrack track = soundManager.getPlayingTrack();
        if (track == null) {
            Console.println("Nothing is playing right now.");
            return;
        }

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
            Console.prln(TrackBox.build(soundManager));
        } else {
            Console.println("The entered number is greater than track duration.");
        }

    }

    @Command(usages = "eq", desc = "Equalizer configuration.")
    public void eq(CommandEvent e) {
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
        soundManager.activeEqualizer();
    }

}