package me.koply.pikap.shell;

import lombok.extern.slf4j.Slf4j;
import me.koply.pikap.sound.MediaFacade;
import me.koply.pikap.util.ArgsHelper;
import me.koply.pikap.util.ColorHelper;
import me.koply.pikap.util.SimilarityCalculator;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

@Slf4j
@Singleton
public class CommandHandler {

    private final CommandRegistry commandRegistry;
    private final MediaFacade mediaFacade;

    // see CommandModule.java
    @Inject
    public CommandHandler(CommandRegistry commandRegistry, MediaFacade mediaFacade) {
        this.commandRegistry = commandRegistry;
        this.mediaFacade = mediaFacade;
    }

    public static final String BANNER = """
               ▄███████▄  ▄█     ▄█   ▄█▄    ▄████████    ▄███████▄\s
              ███    ███ ███    ███ ▄███▀   ███    ███   ███    ███\s
              ███    ███ ███▌   ███▐██▀     ███    ███   ███    ███\s
              ███    ███ ███▌  ▄█████▀      ███    ███   ███    ███\s
            ▀█████████▀  ███▌ ▀▀█████▄    ▀███████████ ▀█████████▀ \s
              ███        ███    ███▐██▄     ███    ███   ███       \s
              ███        ███    ███ ▀███▄   ███    ███   ███       \s
             ▄████▀      █▀     ███   ▀█▀   ███    █▀   ▄████▀     \s
                                ▀                                  \s""";

    public void start() throws IOException {
        Terminal terminal = TerminalBuilder.terminal();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(CommandCompleter.of(commandRegistry.getCommandMap()))
                .build();

        PrintStream originalPrintStream = System.out;
        System.setOut(new PrintStreamWrapper(originalPrintStream, reader));

        terminal.puts(InfoCmp.Capability.flash_screen);
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();

        System.out.println(ColorHelper.red(BANNER));
        System.out.println(ColorHelper.green(ColorHelper.bold("[ Type 'help' for help. Type 'exit' to quit. ]")));

        try {
            while (true) {
                String line = reader.readLine(ColorHelper.bold(ColorHelper.yellow("pikap> ")));
                if (line == null || line.equalsIgnoreCase("exit")) {
                    break;
                }
                line = line.trim();

                String[] parts = line.split("\\s+");
                String command = parts[0];

                if (commandRegistry.getCommandMap().containsKey(command)) {
                    reader.getHistory().add(line);
                    dispatch(line, command, parts, reader);
                }
            }
        } catch (UserInterruptException ignored) {
            System.out.println("bye.");
        } catch (Exception ex) {
            log.error("An error occurred while handling commands.", ex);
        } finally {
            mediaFacade.shutdown();
            terminal.close();
            System.exit(1);
        }
    }

    private void dispatch(String input, String command, String[] parts, LineReader reader) {
        Map<String, String> options = ArgsHelper.parseArgs(parts);
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        var event = new CommandEvent(input, args, command, options, reader);

        var invoker = commandRegistry.getCommandMap().get(command);
        if (invoker != null) {
            invoker.invoke(event);
        } else {
            similarCommands(command);
        }
    }

    private void similarCommands(String command) {
        var commands = commandRegistry.getCommandMap();
        List<String> similarCommands = new ArrayList<>();

        commands.forEach((key, _) -> {
            if (SimilarityCalculator.calculate(command, key) > 0.5) similarCommands.add(key);
        });

        System.out.println(ColorHelper.blue("Similar commands: ") + ColorHelper.yellow(String.join(", ", similarCommands)));
    }

}