package me.koply.pikap.api.cli;

import com.github.tomaslanger.chalk.Chalk;
import me.koply.pikap.Constants;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Console {

    public static final Logger log = Logger.getLogger("NPlayer");
    public static LineReader LINE_READER;

    public static final DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
    static {
        try {
            Terminal terminal = TerminalBuilder.builder().build();
            LINE_READER = LineReaderBuilder.builder().terminal(terminal).build();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }


        log.setUseParentHandlers(false);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                /* Unnecessary for now: final String[] splitted = record.getSourceClassName().split("\\.");
                 final String name = splitted[splitted.length-1]; */
                final String date = formatter.format(new Date(record.getMillis()));
                return Chalk.on("[" + date + "] ").yellow() + ""
                        + Chalk.on("-> ").red().bold()
                        + Chalk.on(record.getMessage()).blue() + "\n";
            }
        });
        log.addHandler(consoleHandler);
    }

    public static void println(String...str) {
        for (String s : str) {
            println(s);
        }
    }

    /**
     * Applies yellow text color to given string
     *
     * @param str text to print
     */
    public static void println(String str) {
        System.out.println(Chalk.on(str).yellow());
    }


    /**
     * Applies yellow text color to given string with prefix
     *
     * @param str text to print
     */
    public static void prefixln(String str) {

        System.out.println(Constants.PREFIX + Chalk.on(str).yellow());
    }

    /**
     * Applies yellow text color to given string
     *
     * @param str text to print
     */
    public static void print(String str) {
        System.out.print(Chalk.on(str).yellow());
    }

    // doest not modify the str so nothing changes
    public static void pr(String str) {
        System.out.print(str);
    }


    // does not modify the str so nothing changes
    public static void prln(String str) {
        System.out.println(str);
    }

    public static void info(String str) {
        log.info(str);
    }

    public static void warn(String str) {
        log.warning(str);
    }


}