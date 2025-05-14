package me.koply.pikap.shell.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.extern.slf4j.Slf4j;
import me.koply.pikap.config.ConfigurationDelegate;
import me.koply.pikap.shell.Command;
import me.koply.pikap.shell.CommandEvent;
import me.koply.pikap.shell.Option;
import me.koply.pikap.sound.AudioProviders;
import me.koply.pikap.sound.MediaFacade;
import me.koply.pikap.util.ArgsHelper;
import me.koply.pikap.util.RegexHelper;
import me.koply.pikap.util.TrackHelper;
import me.koply.pikap.util.TypeParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;

import static me.koply.pikap.util.ColorHelper.*;

@Slf4j
@Singleton
@SuppressWarnings("unused")
public class MediaPlayerCommands extends AudioEventAdapter {

    private final MediaFacade mediaFacade;
    private final ConfigurationDelegate configurationDelegate;

    private final Object lock = new Object();

    @Inject
    public MediaPlayerCommands(MediaFacade mediaFacade, ConfigurationDelegate configurationDelegate) {
        this.mediaFacade = mediaFacade;
        this.configurationDelegate = configurationDelegate;

        mediaFacade.registerEventAdapter(this);
    }

    @Command(usages = {"play", "queue", "p"},
            options = { @Option(name = "youtube", shortName = "y", description = "The youtube link to play or queue.", required = true),
                        @Option(name = "soundcloud", shortName = "sc", description = "The soundcloud link to play or queue.", required = true),
                        @Option(name = "file", shortName = "fil", description = "The file to play or queue.", required = true),
                        @Option(name = "first", shortName = "f", description = "Adds the first element of the search result to the queue."),
                        @Option(name = "help", shortName = "h", description = "Prints this help message.")},
            description = "Plays or queues a track.",
            category = "Music",
            notes = "Hello its last line of the 'help play'")
    public boolean play(CommandEvent event) throws InterruptedException {
        System.out.println(Arrays.toString(event.args()));
        if (event.args().length == 0) {
            var queue = mediaFacade.getQueue();
            System.out.println(TrackHelper.getTrackListInfoWithDuration(queue));
        }

        final var query = new StringBuilder();
        var parsedArgs = event.parsedArgs();

        if (!parsedArgs.isEmpty()) {
            if (ArgsHelper.anyContains(parsedArgs, "--help", "-h")) {
                return false;
            }

            ArgsHelper.getAny(parsedArgs, "--youtube", "-y").ifPresent(s -> {
                if (RegexHelper.isYoutubeURL(s)) {
                    query.append(s);
                } else {
                    query.append("ytsearch:").append(s);
                }
            });

            ArgsHelper.getAny(parsedArgs, "--soundcloud", "-sc").ifPresent(s -> {
                if (RegexHelper.isSoundcloudURL(s)) {
                    query.append(s);
                } else {
                    query.append("scsearch:").append(s);
                }
            });
        }

        if (query.isEmpty()) {
            var selectedAudioProvider = configurationDelegate.get().getDefaultAudioProvider();
            var temp = AudioProviders.fromProviderName(selectedAudioProvider);
            var audioProvider = temp == null ? AudioProviders.SOUNDCLOUD : temp;

            query.append(audioProvider.getSearchPrefix()).append(event.input(), event.pureCommand().length(), event.input().length());
        }
        var queryStr = query.toString();
        System.out.println(blue("Searching for: ") + yellow(queryStr));

        boolean first = ArgsHelper.getAny(parsedArgs, "--first", "-f").isPresent();

        mediaFacade.queryAsync(queryStr, new FunctionalResultHandler(track -> {
            if (first) {
                mediaFacade.play(track);
                System.out.println(TrackHelper.getTrackInfo(track));
            }

            synchronized (lock) {
                lock.notify();
            }
        }, playlist -> {
            if (first && !playlist.getTracks().isEmpty()) {
                var track = playlist.getTracks().getFirst();
                mediaFacade.play(track);
                System.out.println(TrackHelper.getTrackInfo(track));
            } else {
                System.out.println(blue("Search result:"));

                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < playlist.getTracks().size(); i++) {
                    var track = playlist.getTracks().get(i);
                    sb.append(blue("(")).append(blue(String.valueOf(i + 1))).append(blue(") - "));
                    sb.append(yellow(TrackHelper.getTrackInfoInline(track)));
                    sb.append("\n");
                }

                System.out.println(sb);

                System.out.println(blue("Enter the number of the track you want to play. For exit, type '0'."));
                Integer index;
                do {
                    String input = event.reader().readLine(red("(Number)> "));
                    index = TypeParser.parseInt(input);
                } while (index == null || index < 0 || index > playlist.getTracks().size());

                if (index == 0) {
                    synchronized (lock) {
                        lock.notify();
                    }
                    return;
                }

                index -= 1;

                var track = playlist.getTracks().get(index);
                mediaFacade.play(track);
                System.out.println(TrackHelper.getTrackInfo(track));
            }

            synchronized (lock) {
                lock.notify();
            }
        }, () -> {
            System.out.println(red("No results found."));
            synchronized (lock) {
                lock.notify();
            }
        }, exception -> {
            log.error("Failed to search for {}", queryStr, exception);
            System.out.println(red("An error occur while searching."));
            exception.printStackTrace();
            synchronized (lock) {
                lock.notify();
            }
        }));

        synchronized (lock) {
            lock.wait();
        }

        return true;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            var newTrack = mediaFacade.next();
            if (newTrack != null) System.out.println(TrackHelper.getTrackInfo(newTrack));
        }
    }

    @Command(usages = { "pause" },
            description = "Pauses the current track.",
            category = "Music")
    public void pause(CommandEvent event) {
        System.out.println("Track paused.");
        mediaFacade.pause();
    }

    @Command(usages = { "resume" },
            description = "Resumes the current track.",
            category = "Music")
    public void resume(CommandEvent event) {
        System.out.println("Track resumed.");
        mediaFacade.resume();
    }

    @Command(usages = { "stop" },
            description = "Stops the current track.",
            category = "Music")
    public void stop(CommandEvent event) {
        System.out.println("Track stopped.");
        mediaFacade.stop();
    }

    @Command(usages = { "volume" },
            description = "Changes the volume.",
            category = "Music")
    public void volume(CommandEvent event) {
        if (event.args().length == 0) {
            System.out.println(blue("Current volume is ") + red(mediaFacade.getVolume() + ""));
        }
        Integer value = TypeParser.parseInt(event.args()[0]);
        if (value == null || value < 0 || value > configurationDelegate.get().getMaximumVolume()) {
            System.out.println(blue("Volume value must be between 0 and ") + red(configurationDelegate.get().getMaximumVolume()+"") + blue("."));
            return;
        }
        mediaFacade.setVolume(value);
        System.out.println(blue("Volume set to ") + red(value + ""));
    }

    @Command(usages = { "next", "skip", "n"},
            description = "Skips to the next track.",
            category = "Music")
    public void next(CommandEvent event) {
        mediaFacade.next();
    }


    /*
     * new YoutubeAudioSourceManager(true, (String)null, (String)null));
     * new YandexMusicAudioSourceManager(true));
     * SoundCloudAudioSourceManager.createDefault());
     * new BandcampAudioSourceManager());
     * new VimeoAudioSourceManager());
     * new TwitchStreamAudioSourceManager());
     * new BeamAudioSourceManager());
     * new GetyarnAudioSourceManager());
     * new NicoAudioSourceManager());
     * new HttpAudioSourceManager(containerRegistry));
     */

}