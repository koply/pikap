package me.koply.pikap.app;

import lombok.extern.slf4j.Slf4j;
import me.koply.pikap.config.ConfigurationDelegate;

import javax.inject.Inject;

@Slf4j
public class PikapApplication {

    private final ConfigurationDelegate configurationDelegate;

    @Inject
    public PikapApplication(ConfigurationDelegate configurationDelegate) {
        this.configurationDelegate = configurationDelegate;
    }

    public void init() {
        var configuration = configurationDelegate.get();

        // TODO database initialization vs..

    }

}