package me.koply.pikap.util;

import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class SysErrToSlf4J extends OutputStream {

    public static void redirectSysErr(Logger logger) {
        System.setErr(new PrintStream(new SysErrToSlf4J(logger, LogLevel.ERROR)));
    }

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
    private final Logger logger;
    private final LogLevel level;

    public enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR,
    }

    public SysErrToSlf4J(Logger logger, LogLevel level) {
        this.logger = logger;
        this.level = level;
    }

    @Override
    public void write(int b) {
        if (b == '\n') {
            String line = baos.toString();
            baos.reset();

            switch (level) {
                case TRACE:
                    logger.trace(line);
                    break;
                case DEBUG:
                    logger.debug(line);
                    break;
                case ERROR:
                    logger.error(line);
                    break;
                case INFO:
                    logger.info(line);
                    break;
                case WARN:
                    logger.warn(line);
                    break;
            }
        } else {
            baos.write(b);
        }
    }
}