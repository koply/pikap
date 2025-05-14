package me.koply.pikap.di;

import dagger.Module;
import dagger.Provides;
import me.koply.pikap.config.ConfigurationDelegate;
import me.koply.pikap.config.ConfigurationLoader;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class ConfigurationModule {

    @Provides
    @Singleton
    @Named("configPath")
    public String provideConfigPath() {
        return "config.yml";
    }

    @Provides
    @Singleton
    public ConfigurationLoader provideConfigurationLoader(@Named("configPath") String configPath) {
        return new ConfigurationLoader(configPath);
    }

    @Provides
    @Singleton
    public ConfigurationDelegate provideConfigurationDelegate(ConfigurationLoader loader) {
        var delegate = new ConfigurationDelegate();
        loader.subscribe(delegate);
        return delegate;
    }

}