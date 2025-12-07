package me.koply.pikap.di;

import dagger.Module;
import dagger.Provides;
import me.koply.pikap.discordrpc.DiscordRPC;
import me.koply.pikap.sound.MediaFacade;

import javax.inject.Singleton;

@Module
public class DiscordRPCModule {

    @Provides
    @Singleton
    public DiscordRPC providesDiscordRPC(MediaFacade mediaFacade) {
        return new DiscordRPC(mediaFacade);
    }

}
