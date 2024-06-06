package me.koply.pikap.util;

import com.github.tomaslanger.chalk.Chalk;
import me.koply.pikap.sound.SoundManager;

public class StringUtil {
    /**
     * @param seq the object to check
     * @return if param is blank, returns false
     */
    public static boolean notBlank(CharSequence seq) {
        return seq != null && !(seq.length() == 0);
    }

    /**
     * @param seq the object to check
     * @return if param is blank, returns true
     */
    public static boolean isBlank(CharSequence seq) {
        return seq == null || seq.length() == 0;
    }

    /**
     * @param base the base string to compare others
     * @param arr the other strings to compare with base
     * @return if any string equals without case sensitivity, returns true
     */
    public static boolean anyEqualsIgnoreCase(String base, String...arr) {
        for (String seq : arr) {
            if (base.equalsIgnoreCase(seq)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param base the base string to compare others
     * @param arr the other strings to compare with base
     * @return if any string equals with case sensitivity, returns true
     */
    public static boolean anyEquals(String base, String...arr) {
        for (String seq : arr) {
            if (base.equals(seq)) {
                return true;
            }
        }
        return false;
    }

    public static String getCurrentTimeYellow() {
        return Chalk.on("[" + Util.getCurrentTime() + "]").yellow().toString();
    }

    public static String getTrackBoxWithCurrentTime(SoundManager soundManager) {
        return TrackBox.build(soundManager) + " " + getCurrentTimeYellow();
    }
}