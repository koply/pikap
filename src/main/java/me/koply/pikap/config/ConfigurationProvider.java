package me.koply.pikap.config;


public interface ConfigurationProvider {
    void load();
    String get(String key);
}
