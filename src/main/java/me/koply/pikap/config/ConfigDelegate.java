package me.koply.pikap.config;

import me.koply.pikap.util.architechture.ObserverDelegate;

import java.util.function.Supplier;

public class ConfigDelegate<T> extends ObserverDelegate<T> {
    public ConfigDelegate(Supplier<T> loadSupplier) {
        super(loadSupplier);
    }

    public void registerSelf(ConfigurationProvider provider) {
        provider.addObserver(this);
    }
}
