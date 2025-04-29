package me.koply.pikap.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Singleton
public class ConfigurationLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationLoader.class);

    private final ObjectMapper mapper;
    private final Path configPath;
    private Configuration configuration;

    @Inject
    public ConfigurationLoader(@Named("configPath") String configPath) {
        this.mapper = new ObjectMapper(new YAMLFactory());
        this.configPath = Paths.get(configPath);
        loadConfiguration();
    }

    public void loadConfiguration() {
        try {
            File configFile = configPath.toFile();

            if (!configFile.exists()) {
                createDefaultConfig(configFile);
                LOGGER.info("Default configuration file created successfully.");
            }

            this.configuration = mapper.readValue(configFile, Configuration.class);
            LOGGER.info("Configuration loaded successfully.");
        } catch (IOException e) {
            LOGGER.error("Failed to load configuration, falling back to defaults.", e);
            this.configuration = new Configuration(false, "sqlite", "data.db", -1, false, "./recordings/", 8, 100);
        }
    }

    private void createDefaultConfig(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        ConfigurationGenerator.buildDefaultConfig(stringBuilder);

        Files.writeString(file.toPath(), stringBuilder);
    }

}