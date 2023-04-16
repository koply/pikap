package me.koply.pikap.api.cli.command;

import com.github.tomaslanger.chalk.Chalk;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.sound.SoundManager;
import org.jline.reader.UserInterruptException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {

    public static final Map<String, CommandClassData> COMMAND_CLASSES = new HashMap<>();
    private static final String CONSOLE_PREFIX = "> ";

    private final CommandInitializer initializer = new CommandInitializer(COMMAND_CLASSES);

    public CommandHandler(String...packages) {
        initializer.registerPackage(packages);
    }

    public void registerInstance(CLICommand command) {
        initializer.registerInstance(command);
    }

    public static boolean isRunning = true;

    /**
     * Blocks the caller thread for handle commands from cli.
     */
    public void startNewHandler() {
        while (isRunning) {
            String entry = this.readUserInput();
            String[] args = entry.split(" ");

            if (entry.equalsIgnoreCase("exit") || entry.equalsIgnoreCase("quit")) {
                SoundManager.shutdown();
                // TODO close the KeyboardListener
                break;
            }

            if (!COMMAND_CLASSES.containsKey(args[0])) {
                Console.println(Chalk.on("[NPLAYER]").red() + " There is no command for this entry.");
                continue;
            }

            CommandClassData ccd = COMMAND_CLASSES.get(args[0]);
            CommandEvent event = new CommandEvent(args, entry);
            callCommandMethod(event, ccd);
        }
    }

    private void callCommandMethod(CommandEvent event, CommandClassData ccd) {
        try {
            ccd.methods.get(event.getArgs()[0]).method.invoke(ccd.instance, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private String readUserInput() {
        try {
            String prefix = Chalk.on(CONSOLE_PREFIX).yellow().toString();
            return Console.LINE_READER.readLine(prefix).trim();
        } catch (UserInterruptException e) {
            isRunning = false;
            return "exit";
        }
    }
}