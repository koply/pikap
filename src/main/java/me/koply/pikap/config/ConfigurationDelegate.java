package me.koply.pikap.config;

import me.koply.pikap.util.architecture.Delegate;
import me.koply.pikap.util.architecture.Subscriber;

import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class ConfigurationDelegate implements Delegate<Configuration>, Subscriber<Configuration> {

    private final AtomicReference<Configuration> configuration = new AtomicReference<>();

    @Override
    public Configuration get() {
        return configuration.get();
    }

    @Override
    public void accept(Configuration configuration) {
        this.configuration.set(configuration);
    }

    @Override
    public Class<Configuration> getType() {
        return Configuration.class;
    }
}