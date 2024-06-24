package me.koply.pikap.api.cli.command;

import me.koply.pikap.api.cli.Console;

public interface CLICommand {
    default void println(String o) {
        Console.println(o);
    }

    default void prefixln(String o) {
        Console.prefixln(o);
    }
}