package me.koply.pikap;


import me.koply.pikap.api.cli.Console;
import me.koply.pikap.config.ConfigurationProvider;
import me.koply.pikap.config.YMLConfigurationProvider;
import me.koply.pikap.database.DatabaseAccessObject;

public class Application {

    public static final String VERSION = "0.2.0-beta";

    public static void main(String[] args) {
        new Application();
    }

    private final ConfigurationProvider configurationProvider;

    public Application() {
        // Configuration
        configurationProvider = new YMLConfigurationProvider("config.yml");
        configurationProvider.createDefault();
        configurationProvider.load();

        if(!configurationProvider.isLoaded()) {
            Console.warn(Constants.PREFIX + "An error has occurred while loading the configuration file!");
            System.exit(1);
        }

        // check config.get("db") disable or sqlite
        DatabaseAccessObject databaseAccessObject = new DatabaseAccessObject();



        configurationProvider.addObserver(databaseAccessObject);


    }
}
