package me.koply.pikap.app;

import lombok.extern.slf4j.Slf4j;
import me.koply.pikap.config.ConfigurationDelegate;
import me.koply.pikap.discordrpc.DiscordRPC;

import javax.inject.Inject;

@Slf4j
public class PikapApplication {

    private final ConfigurationDelegate configurationDelegate;
    private final DiscordRPC discordRPC;

    @Inject
    public PikapApplication(ConfigurationDelegate configurationDelegate, DiscordRPC discordRPC) {
        this.configurationDelegate = configurationDelegate;
        this.discordRPC = discordRPC;
    }

    public void init() {
        var configuration = configurationDelegate.get();
        // TODO database initialization vs..



        var discordRPCstatus = discordRPC.prepare();
        if (!discordRPCstatus) {
            log.error("DiscordRPC cannot be initialized.");
        } else {
            log.info("DiscordRPC initialized successfully.");
            discordRPC.loadAsync();
        }
    }

}