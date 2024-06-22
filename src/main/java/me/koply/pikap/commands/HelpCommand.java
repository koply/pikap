package me.koply.pikap.commands;

import com.github.tomaslanger.chalk.Chalk;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.cli.command.*;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static me.koply.pikap.api.cli.command.CommandClassData.MethodAndAnnotation;

public class HelpCommand implements CLICommand {

    private String helpMessage;
    public String getHelpMessage() {
        if (helpMessage != null) return helpMessage;
        StringBuilder sb = new StringBuilder();
        Set<Method> duplicateChecker = new HashSet<>();
        for (Map.Entry<String, CommandClassData> entry : CommandHandler.COMMAND_CLASSES.entrySet()) {
            CommandClassData ccd = entry.getValue();
            for (Map.Entry<String, MethodAndAnnotation> innerEntry : ccd.methods.entrySet()) {
                if (duplicateChecker.contains(innerEntry.getValue().method)) continue;
                sb.append(Chalk.on(innerEntry.getKey()).red())
                        .append(Chalk.on(": ").red().bold())
                        .append(Chalk.on(innerEntry.getValue().annotation.desc()).blue()).append("\n");
                duplicateChecker.add(innerEntry.getValue().method);
            }
        }
        helpMessage = sb.toString();
        return helpMessage;
    }

    // TODO: Categorise all commands
    @Command(usages = {"help", "commands"}, desc = "This command.")
    public void som(CommandEvent e) {
        Console.println(getHelpMessage());
    }

}