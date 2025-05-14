package me.koply.pikap.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.koply.pikap.util.TypeParser;
import me.koply.pikap.util.architecture.Publisher;
import me.koply.pikap.util.architecture.Subscriber;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Singleton
@Slf4j
public class ConfigurationLoader implements Publisher<Configuration> {

    @Getter
    private final List<Subscriber<Configuration>> subscribers = new CopyOnWriteArrayList<>();
    private volatile Configuration configuration;

    @Inject
    public ConfigurationLoader(@Named("configPath") String configPath) {
        loadConfiguration(Paths.get(configPath));
    }

    private void loadConfiguration(Path configPath) {
        try {
            var mapper = new ObjectMapper(new YAMLFactory());
            File configFile = configPath.toFile();

            if (!configFile.exists()) {
                createDefaultConfig(configFile);
                log.info("Default configuration file created successfully.");
            }

            this.configuration = mapper.readValue(configFile, Configuration.class);
            publish(this.configuration);
        } catch (UnrecognizedPropertyException ignored) {
        } catch (IOException e) {
            log.error("Failed to load configuration file, falling back to defaults.", e);

            this.configuration = new Configuration();
            fillDefaultValues(this.configuration);
            publish(this.configuration);

        }  finally {
            log.info("Configuration loaded successfully.");
        }
    }

    private void createDefaultConfig(@NotNull File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        ConfigurationGenerator.buildDefaultConfig(stringBuilder);

        Files.writeString(file.toPath(), stringBuilder);
    }

    private void fillDefaultValues(Configuration configuration) {
        Field[] fields = Configuration.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
            if (annotation != null) {
                String value = annotation.defaultValue();
                try {
                    field.set(configuration, TypeParser.parse(value, annotation.type()));
                } catch (IllegalArgumentException | IllegalAccessException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }

    @Override
    public void subscribe(Subscriber<Configuration> subscriber) {
        subscriber.accept(configuration);
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber<Configuration> subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void publish(Configuration configuration) {
        subscribers.forEach(subscriber -> subscriber.accept(configuration));
    }
}