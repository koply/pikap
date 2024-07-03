package me.koply.pikap.config;

import me.koply.pikap.util.YMLReader;
import me.koply.pikap.util.architechture.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Singleton
public class YMLConfigurationProvider implements ConfigurationProvider {

    private final Path configurationPath;

    private final Map<String, String> dataMap = new ConcurrentHashMap<>();
    private final Set<Observer> observers = new CopyOnWriteArraySet<>();

    @Inject
    public YMLConfigurationProvider(String fileName) {
        this.configurationPath = Path.of(fileName);
    }

    @Override
    public void createDefault() {
        if(Files.exists(configurationPath)) return;

        String name = "/" + configurationPath;
        try(InputStream stream = this.getClass().getResourceAsStream(name)) {
            if (stream == null) throw new IOException("File " + name + " not found in resources!");
            Files.copy(stream, configurationPath);
        } catch (IOException e) {
            // TODO BETTER LOGGGING
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        dataMap.clear();
        YMLReader.readTo(configurationPath, dataMap);
        observers.forEach(Observer::update);
    }

    @Override
    public String get(String key) {
        return dataMap.get(key);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public boolean isLoaded() {
        return !dataMap.isEmpty();
    }
}
