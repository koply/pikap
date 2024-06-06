package me.koply.pikap.util;

import me.koply.pikap.api.cli.Console;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Util {

    private static final long DAY = 86_400_000L;
    private static final long HOUR = 3_600_000L;
    private static final long MINUTE = 60_000L;
    private static final long SECOND = 1_000L;

    public static boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public static Integer parseInt(String entry) {
        try {
            return Integer.parseInt(entry);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Float parseFloat(String entry) {
        try {
            return Float.parseFloat(entry);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getCurrentTime() {
        return Console.formatter.format(new Date(System.currentTimeMillis()));
    }

    public static String formatMilliSecond(long millis) {
        long days = millis / DAY;
        millis -= days * DAY;

        long hours = millis / HOUR;
        millis -= hours * HOUR;

        long minutes = millis / MINUTE;
        millis -= minutes * MINUTE;

        long seconds = millis / SECOND;

        StringBuilder builder = new StringBuilder();

        if (days > 0) {
            builder.append(days).append(days == 1 ? " day" : " days");
        }
        if (hours > 0) {
            builder.append(" ").append(hours).append(hours == 1 ? " hour" : " hours");
        }
        if (minutes > 0) {
            builder.append(" ").append(minutes).append(minutes == 1 ? " minute" : " minutes");
        }
        if (seconds > 0) {
            builder.append(" ").append(seconds).append(seconds == 1 ? " second" : " seconds");
        }
        return builder.toString().trim();
    }

    public static int readInteger(String text) {
        Console.println(text + " (Number)");
        while (true) {
            Console.print("> ");
            String entry = Console.SC.nextLine();
            Integer value = parseInt(entry);
            if (value != null) return value;
        }
    }

    public static int readInteger(String text, int min, int max) {
        Console.println(text + " (Only numbers with minimum: " + min + ", maximum: " + max + ", 0 for cancel)");
        while (true) {
            Console.print("> ");
            String entry = Console.SC.nextLine();
            Integer value = parseInt(entry);
            if (value == null) {
                Console.println(text + " (Only numbers with minimum: " + min + ", maximum: " + max + ", 0 for cancel)");
                continue;
            }
            if (value == 0) return 0;
            if (value >= min && value <= max) return value;
        }
    }

    public static String getTrackFileName() {
        // PTF -> Pikap Track File
        return "track_" + getDateForFileName() + ".ptf";
    }

    public static String getLogName() {
        return "pikap_" + getDateForFileName() + ".log";
    }

    public static String getDateForFileName() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        return formatter.format(date);
    }

}