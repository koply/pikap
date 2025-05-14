package me.koply.pikap.di;

import dagger.Module;
import dagger.Provides;
import me.koply.pikap.shell.CommandHandler;
import me.koply.pikap.shell.CommandRegistry;
import me.koply.pikap.sound.MediaFacade;

import javax.inject.Singleton;

@Module
public class CommandHandlerModule {

    @Provides
    @Singleton
    public CommandHandler providesCommandHandler(CommandRegistry commandRegistry, MediaFacade mediaFacade) {
        return new CommandHandler(commandRegistry, mediaFacade);
    }
}