package me.koply.pikap;

import com.github.tomaslanger.chalk.Chalk;

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


}