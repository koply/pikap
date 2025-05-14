package me.koply.pikap.shell;

import org.jline.reader.LineReader;

import java.util.Map;

public record CommandEvent(String input, String[] args, String pureCommand, Map<String, String> parsedArgs, LineReader reader) {
}