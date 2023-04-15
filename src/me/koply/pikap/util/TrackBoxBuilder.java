package me.koply.pikap.util;

import com.github.tomaslanger.chalk.Ansi;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class TrackBoxBuilder {
    private static final String TOP_LEFT_CORNER = "\u2554";
    private static final String TOP_RIGHT_CORNER = "\u2557";
    private static final String BOTTOM_LEFT_CORNER = "\u255a";
    private static final String BOTTOM_RIGHT_CORNER = "\u255d";
    private static final String BORDER_HORIZONTAL = "\u2550";
    private static final String BORDER_VERTICAL = "\u2551";
    private static final String PROGRESS_FILL = "\u25a0";
    private static final String PROGRESS_EMPTY = "\u2015";

    public static String buildTrackBox(int width, AudioTrack track, boolean isPaused, int volume) {
        return boxify(width, buildFirstLine(width - 4, track), buildSecondLine(width - 4, track, isPaused, volume));
    }

    private static String buildFirstLine(int width, AudioTrack track) {
        StringBuilder builder = new StringBuilder();
        String title = track.getInfo().title;
        int titleWidth = width - 7;

        if (title.length() > titleWidth) {
            builder.append(title, 0, titleWidth - 3);
            builder.append("...  ");
            builder.append(" ".repeat(6));
        } else {
            builder.append(title);
            builder.append(" ".repeat( (width-title.length()) + 1 ));
        }

        return builder.toString();
    }

    private static String buildSecondLine(int width, AudioTrack track, boolean isPaused, int volume) {
        String cornerText = isPaused ? "PAUSED" : volume + "%";

        String duration = formatTiming(track.getDuration(), track.getDuration());
        String position = formatTiming(track.getPosition(), track.getDuration());
        int spacing = duration.length() - position.length();
        int barLength = width - duration.length() - position.length() - spacing - 14;

        float progress = (float) Math.min(track.getPosition(), track.getDuration()) / (float) Math.max(track.getDuration(), 1);
        int progressBlocks = Math.round(progress * barLength);

        StringBuilder builder = new StringBuilder();

        builder.append(" ".repeat(Math.max(0, 6 - cornerText.length())));

        builder.append(cornerText);

        builder.append(" [");
        for (int i = 0; i < barLength; i++) {
            builder.append(i < progressBlocks ? PROGRESS_FILL : PROGRESS_EMPTY);
        }
        builder.append("]");

        builder.append(" ".repeat(Math.max(0, spacing + 1)));

        builder.append(position);
        builder.append(" of ");
        builder.append(duration);

        builder.append(" ");

        return builder.toString();
    }

    private static String formatTiming(long timing, long maximum) {
        timing = Math.min(timing, maximum) / 1000;

        long seconds = timing % 60;
        timing /= 60;
        long minutes = timing % 60;
        timing /= 60;
        long hours = timing;

        if (maximum >= 3600000L) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%d:%02d", minutes, seconds);
        }
    }

    private static void boxifyLine(StringBuilder builder, String line, Ansi.Color lineColor, Ansi.Color borderColor) {
        builder.append(borderColor.getStart());
        builder.append(BORDER_VERTICAL);
        builder.append(borderColor.getEnd());
        builder.append(" ");
        builder.append(lineColor.getStart());

        builder.append(line);

        builder.append(lineColor.getEnd());
        builder.append(borderColor.getStart());
        builder.append(BORDER_VERTICAL);
        builder.append(borderColor.getEnd());
        builder.append("\n");
    }

    private static String boxify(int width, String firstLine, String secondLine) {
        StringBuilder builder = new StringBuilder();

        // frame color
        Ansi.Color frameColor = Ansi.Color.BLUE;
        Ansi.Color firstLineColor = Ansi.Color.YELLOW;
        builder.append(frameColor.getStart());

        builder.append(TOP_LEFT_CORNER);
        String straightLine = BORDER_HORIZONTAL.repeat(Math.max(0, width - 2));
        builder.append(straightLine);
        builder.append(TOP_RIGHT_CORNER);
        builder.append(frameColor.getEnd()).append("\n");

        boxifyLine(builder, firstLine, firstLineColor, frameColor);
        boxifyLine(builder, secondLine, firstLineColor, frameColor);

        builder.append(frameColor.getStart());
        builder.append(BOTTOM_LEFT_CORNER);
        builder.append(straightLine);
        builder.append(BOTTOM_RIGHT_CORNER);
        builder.append(frameColor.getEnd());

        return builder.toString();
    }
}