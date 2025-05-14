package me.koply.pikap.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper {

    private static final Pattern youtubePattern = Pattern.compile("^((?:https?:)?//)?((?:www|m)\\.)?(youtube\\.com|youtu.be)(/(?:[\\w\\-]+\\?v=|embed/|v/)?)([\\w\\-]+)(\\S+)?$");
    private static final Pattern soundcloudPattern = Pattern.compile("^https?://((on\\.)?soundcloud\\.com|snd\\.sc)/(.*)$");

    public static boolean isYoutubeURL(String str) {
        Matcher matcher = youtubePattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isSoundcloudURL(String str) {
        Matcher matcher = soundcloudPattern.matcher(str);
        return matcher.matches();
    }

}