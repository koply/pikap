package me.koply.pikap.commands;

import com.github.tomaslanger.chalk.Chalk;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.cli.command.CLICommand;
import me.koply.pikap.api.cli.command.Command;
import me.koply.pikap.api.cli.command.CommandEvent;

import java.util.Map;

public class ConfigCommands implements CLICommand {

    @Command(usages = "config", desc = "Shows the selected configurations.")
    public void viewConfig(CommandEvent e) {
        Console.println(Chalk.on("Config path: ").yellow() + Main.CONFIG.file.getPath());
        for (Map.Entry<String, String> entry : Main.CONFIG.entrySet()) {
            Console.println(Chalk.on(entry.getKey()).blue() + ": " + entry.getValue());
        }
    }

}