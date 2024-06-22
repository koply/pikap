package me.koply.pikap.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    private static final Pattern youtubePattern = Pattern.compile("^((?:https?:)?//)?((?:www|m)\\.)?(youtube\\.com|youtu.be)(/(?:[\\w\\-]+\\?v=|embed/|v/)?)([\\w\\-]+)(\\S+)?$");

    public static boolean isYoutubeMatch(String str) {
        Matcher matcher = youtubePattern.matcher(str);
        return matcher.matches();
    }
}
