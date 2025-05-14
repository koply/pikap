package me.koply.pikap.di;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import me.koply.pikap.shell.commands.MediaPlayerCommands;
import me.koply.pikap.shell.commands.TestCommands;

@Module
public interface CommandModule {

    @Binds
    @IntoSet
    Object bindTestCommands(TestCommands impl);

    @Binds
    @IntoSet
    Object bindMediaPlayerCommands(MediaPlayerCommands impl);

}