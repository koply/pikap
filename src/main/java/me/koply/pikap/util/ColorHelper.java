package me.koply.pikap.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ColorHelper {

    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String ITALIC = "\u001B[3m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String BLINK = "\u001B[5m";
    public static final String REVERSE = "\u001B[7m";
    public static final String HIDDEN = "\u001B[8m";
    public static final String STRIKETHROUGH = "\u001B[9m";

    public static final String DEFAULT = "\u001B[39m";
    public static final String DEFAULT_BACKGROUND = "\u001B[49m";

    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BRIGHT_BLACK = "\u001B[90m";
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_PURPLE = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_WHITE = "\u001B[97m";

    public static final String BG_BLACK = "\u001B[40m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_PURPLE = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    public static final String BG_WHITE = "\u001B[47m";

    @Contract(pure = true)
    public static @NotNull String bold(String str) {
        return BOLD + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String italic(String str) {
        return ITALIC + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String underline(String str) {
        return UNDERLINE + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String black(String str) {
        return BLACK + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String red(String str) {
        return RED + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String green(String str) {
        return GREEN + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String yellow(String str) {
        return YELLOW + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String blue(String str) {
        return BLUE + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String purple(String str) {
        return PURPLE + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String cyan(String str) {
        return CYAN + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String white(String str) {
        return WHITE + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String brightBlack(String str) {
        return BRIGHT_BLACK + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String brightRed(String str) {
        return BRIGHT_RED + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String brightGreen(String str) {
        return BRIGHT_GREEN + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String brightYellow(String str) {
        return BRIGHT_YELLOW + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String brightBlue(String str) {
        return BRIGHT_BLUE + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String brightPurple(String str) {
        return BRIGHT_PURPLE + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String brightCyan(String str) {
        return BRIGHT_CYAN + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String brightWhite(String str) {
        return BRIGHT_WHITE + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String bgBlack(String str) {
        return BG_BLACK + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String bgRed(String str) {
        return BG_RED + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String bgGreen(String str) {
        return BG_GREEN + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String bgYellow(String str) {
        return BG_YELLOW + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String bgBlue(String str) {
        return BG_BLUE + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String bgPurple(String str) {
        return BG_PURPLE + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String bgCyan(String str) {
        return BG_CYAN + str + RESET;
    }

    @Contract(pure = true)
    public static @NotNull String bgWhite(String str) {
        return BG_WHITE + str + RESET;
    }

}