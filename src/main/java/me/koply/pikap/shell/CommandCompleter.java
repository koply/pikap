package me.koply.pikap.shell;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.*;

public class CommandCompleter implements Completer {

    private CommandCompleter() {}

    private final Map<String, String[]> commandOptions = new HashMap<>();

    public static CommandCompleter of(Map<String, MethodInvoker> commandMap) {
        return new CommandCompleter().build(commandMap);
    }

    private CommandCompleter build(Map<String, MethodInvoker> commandMap) {
        commandMap.forEach((command, invoker) -> {
            commandOptions.put(command, getOptions(invoker.getCommandAnnotation().options()));

            if (invoker.getTarget() instanceof CompletionProvider provider) {
                commandOptions.putAll(provider.getCompletions());
            }
        });
        return this;
    }

    private String[] getOptions(Option...options) {
        String[] optionsString = new String[options.length*2];

        for (int i = 0; i < options.length; i++) {
            optionsString[i*2] = "--" + options[i].name();
            optionsString[i*2 + 1] = "-" + options[i].shortName();
        }

        return optionsString;
    }

    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        int wordIndex = parsedLine.wordIndex();
        String firstWord = parsedLine.words().getFirst();

        if (wordIndex == 0) {
            commandOptions.keySet().forEach(command -> list.add(new Candidate(command)));
        } else if (wordIndex > 0 && commandOptions.containsKey(firstWord) && commandOptions.get(firstWord).length > 0) {
            String[] options = commandOptions.get(firstWord);
            for (String option : options) {
                list.add(new Candidate(option));
            }
        }
    }
}