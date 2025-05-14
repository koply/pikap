package me.koply.pikap.util;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static me.koply.pikap.util.ColorHelper.*;

public class TrackHelper {

    public static String getTrackInfo(AudioTrack track) {
        long duration = track.getDuration();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes);

        var info = track.getInfo();
        return String.format("%s (%s) [id: %s, uri: %s] %02d:%02d", info.title, info.author, info.identifier, info.uri, minutes, seconds);
    }

    public static String getTrackInfoInline(AudioTrack track) {
        long duration = track.getDuration();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes);
        var info = track.getInfo();
        return String.format("[%02d:%02d] %s (%s)", minutes, seconds, info.title, info.author);
    }

    public static String getTrackListInfo(Collection<AudioTrack> tracks) {
        StringBuilder sb = new StringBuilder();

        int i = 1;
        for (AudioTrack track : tracks) {
            sb.append(blue("(")).append(blue(String.valueOf(i + 1))).append(blue(") - "));
            sb.append(yellow(TrackHelper.getTrackInfoInline(track)));
            sb.append("\n");
        }

        return sb.toString();
    }

    public static String getTrackListInfoWithDuration(Collection<AudioTrack> tracks) {
        StringBuilder sb = new StringBuilder();

        long totalDuration = 0;

        int i = 1;
        for (AudioTrack track : tracks) {
            sb.append(blue("(")).append(blue(String.valueOf(i + 1))).append(blue(") - "));
            sb.append(yellow(TrackHelper.getTrackInfoInline(track)));
            sb.append("\n");
            totalDuration += track.getDuration();
        }

        sb.append(blue("Total "))
                .append(red(i + ""))
                .append(blue(" tracks with "))
                .append(red(String.valueOf(TimeUnit.MILLISECONDS.toMinutes(totalDuration))))
                .append(blue(" minutes and "))
                .append(red(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(totalDuration) - TimeUnit.MINUTES.toSeconds(totalDuration))))
                .append(blue(" seconds."));

        return sb.toString();
    }

}