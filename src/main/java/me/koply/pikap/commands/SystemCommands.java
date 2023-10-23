package me.koply.pikap.commands;

import me.koply.pikap.api.cli.command.CLICommand;
import me.koply.pikap.api.cli.command.Command;
import me.koply.pikap.api.cli.command.CommandEvent;

public class SystemCommands implements CLICommand {

    @Command(usages = "gc", desc = "Calls the garbage collector.")
    public void gc(CommandEvent e) {
        System.gc();
        println("Garbage collector called.");
    }

}