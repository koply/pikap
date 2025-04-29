package me.koply.pikap.config;

import dagger.Module;
import dagger.Provides;

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

}