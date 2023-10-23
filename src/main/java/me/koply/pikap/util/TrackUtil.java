package me.koply.pikap.util;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

public class TrackUtil {

    public static String trackToString(AudioTrack track) {
        return trackToString(track.getInfo());
    }

    public static String trackToString(AudioTrackInfo info) {
        return info.author + " - " + info.title + " ["+ Util.formatMilliSecond(info.length) +"]";
    }

}