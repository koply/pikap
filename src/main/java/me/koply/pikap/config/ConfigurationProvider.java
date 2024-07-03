package me.koply.pikap.config;


import me.koply.pikap.util.architechture.Observable;

public interface ConfigurationProvider extends Observable {
    void createDefault();
    void load();
    String get(String key);
}
