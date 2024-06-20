package me.koply.pikap.commands;

import com.github.tomaslanger.chalk.Chalk;
import me.koply.pikap.Constants;
import me.koply.pikap.api.cli.command.CLICommand;
import me.koply.pikap.api.cli.command.Command;
import me.koply.pikap.api.cli.command.CommandEvent;
import me.koply.pikap.session.SessionData;

public class SystemCommands implements CLICommand {

    @Command(usages = "gc", desc = "Calls the garbage collector.")
    public void gc(CommandEvent e) {
        System.gc();
        println("Garbage collector called.");
    }

    @Command(usages = "memory", desc = "Shows the memory usage statistics.")
    public void memory(CommandEvent e) {
        Runtime rt = Runtime.getRuntime();
        float total_mem = (float) rt.totalMemory() / 1_000_000;
        float free_mem = (float) rt.freeMemory() / 1_000_000;
        float used_mem = total_mem - free_mem;
        float max_mem = (float) rt.maxMemory() / 1_000_000;

        println("Max memory: " + Chalk.on(max_mem + " MB").blue());
        println("Allocated memory: " + Chalk.on(total_mem + " MB").blue());
        println("Free memory: " + Chalk.on(free_mem + " MB").blue());
        println("Memory used: " + Chalk.on(used_mem + " MB").blue());
    }

    @Command(usages = "net", desc = "Network activity statistics.")
    public void net(CommandEvent e) {
        long cycles = SessionData.bufferCycles.get();
        long bytes = Constants.AUDIO_BUFFER_SIZE;
        double totalMegaBytes = (double) bytes*cycles / 1_000_000;
        println("Buffer cycle count: " + Chalk.on(cycles+"").blue());
        println("Total used buffer (x" + bytes + "): " + Chalk.on(totalMegaBytes + " MB").blue());
    }

}