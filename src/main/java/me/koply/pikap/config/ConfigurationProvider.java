package me.koply.pikap.config;


import me.koply.pikap.util.architechture.Observable;

import java.util.Map;

public interface ConfigurationProvider extends Observable {
    void createDefault();
    boolean isLoaded();
    void load();
    String get(String key);
    String getOrDefault(String key, String defaultValue);
    Map<String, String> getDataMap();
}
