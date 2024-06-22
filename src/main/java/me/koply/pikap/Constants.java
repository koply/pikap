package me.koply.pikap;

import com.github.tomaslanger.chalk.Chalk;

import java.io.File;

import static com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats.COMMON_PCM_S16_BE;

public class Constants {

    public static final String VERSION = "1.0.0-alpha";

    public static final String BANNER = " ██▓███   ██▓ ██ ▄█▀▄▄▄       ██▓███  \n" +
            "▓██░  ██▒▓██▒ ██▄█▒▒████▄    ▓██░  ██▒\n" +
            "▓██░ ██▓▒▒██▒▓███▄░▒██  ▀█▄  ▓██░ ██▓▒\n" +
            "▒██▄█▓▒ ▒░██░▓██ █▄░██▄▄▄▄██ ▒██▄█▓▒ ▒\n" +
            "▒██▒ ░  ░░██░▒██▒ █▄▓█   ▓██▒▒██▒ ░  ░\n" +
            "▒▓▒░ ░  ░░▓  ▒ ▒▒ ▓▒▒▒   ▓▒█░▒▓▒░ ░  ░\n" +
            "░▒ ░      ▒ ░░ ░▒ ▒░ ▒   ▒▒ ░░▒ ░     \n" +
            "░░        ▒ ░░ ░░ ░  ░   ▒   ░░       \n" +
            "          ░  ░  ░        ░  ░         \n";

    public static final String FIRST_LINE = Chalk.on("[").red().bold().toString() +
            Chalk.on(" Type 'help' for available commands list. ").yellow() + Chalk.on("]").red().bold();

    public static final String PREFIX = Chalk.on("[PIKAP] ").red().toString();

    public static final int AUDIO_BUFFER_SIZE = COMMON_PCM_S16_BE.maximumChunkSize();

    public static final String YT_URL_PREFIX = "https://www.youtube.com/watch?v=";

    public static final String YT_PLIST_URL_PREFIX = "https://www.youtube.com/playlist?list=";

    // SUM
    public static final byte[] PTF_HEADER_PREFIX = new byte[] { 0x53, 0x55, 0x4D };

    public static final File CONFIG_FILE = new File("./config.yml");

    public static final File LOGS_FOLDER = new File("logs/");

    public static final File LIB_FOLDER = new File("lib/");
}