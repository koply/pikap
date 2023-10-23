package me.koply.pikap.api.cli.command;

public class CommandEvent {

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
}