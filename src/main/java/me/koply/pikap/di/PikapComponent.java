package me.koply.pikap.di;

import dagger.Component;
import me.koply.pikap.app.PikapApplication;
import me.koply.pikap.discordrpc.DiscordRPC;
import me.koply.pikap.shell.CommandHandler;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ConfigurationModule.class, CommandModule.class, CommandHandlerModule.class, DiscordRPCModule.class})
public interface PikapComponent {

    PikapApplication getPikapApplication();
    CommandHandler getCommandHandler();

}