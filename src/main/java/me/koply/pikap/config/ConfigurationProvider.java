package me.koply.pikap.config;


public interface ConfigurationProvider {
    void createDefault();
    void load();
    String get(String key);
}
