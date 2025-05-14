package me.koply.pikap.shell.commands;

import me.koply.pikap.config.ConfigurationDelegate;
import me.koply.pikap.shell.Command;
import me.koply.pikap.shell.CommandEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TestCommands {

    private final ConfigurationDelegate configurationDelegate;

    @Inject
    public TestCommands(ConfigurationDelegate configurationDelegate) {
        this.configurationDelegate = configurationDelegate;
    }

    @Command(usages = "hello")
    public void sayHello(CommandEvent event) {
        System.out.println("hello, its hello command.");

    }

    @Command(usages = "bye")
    public void sayBye(CommandEvent event) {
        System.out.println("bye, its bye command.");
    }

}