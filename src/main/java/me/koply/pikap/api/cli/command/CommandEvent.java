package me.koply.pikap.api.cli.command;

import me.koply.pikap.event.Event;

public class CommandEvent implements Event {

    public CommandEvent(String[] args, String pureCommand) {
        this.args = args;
        this.pureCommand = pureCommand;
    }

    private final String[] args;
    private final String pureCommand;

    public String[] getArgs() {
        return args;
    }

    public String getPureCommand() {
        return pureCommand;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }
}