package me.koply.pikap.config;

import me.koply.pikap.util.architechture.ObserverDelegate;

import javax.inject.Inject;
import java.util.function.Supplier;

public class ConfigDelegate<T> extends ObserverDelegate<T> {
    public ConfigDelegate(Supplier<T> loadSupplier) {
        super(loadSupplier);
    }

    @Inject
    public void register(ConfigurationProvider provider) {
        provider.addObserver(this);
    }
}
