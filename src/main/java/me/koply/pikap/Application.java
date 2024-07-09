package me.koply.pikap;

import lombok.Getter;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.config.ConfigurationProvider;
import me.koply.pikap.config.YMLConfigurationProvider;
import me.koply.pikap.database.*;
import me.koply.pikap.database.ormlite.SqliteConnectionController;
import me.koply.pikap.database.ormlite.DAOHolder;
import me.koply.pikap.event.EventPublisher;

public class Application {

    public static final String VERSION = "0.2.0-beta";












    1

    public static void main(String[] args) {
        instance = new Application();
    }

    @Getter
    private final ConfigurationProvider configurationProvider;

    @Getter
    private final DatabaseAccessDelegate databaseAccessDelegate;

    public Application() {
        // Configuration
        configurationProvider = new YMLConfigurationProvider("config.yml");
        configurationProvider.createDefault();

        databaseAccessDelegate = createDatabaseAccessDelegate();

        configurationProvider.load();
        if(!configurationProvider.isLoaded()) {
            Console.warn(Constants.PREFIX + "An error has occurred while loading the configuration file!");
            System.exit(1);
        }

        // null check when DisabledOrmLiteConnectionController
        // if null we don't need a listener
        if (databaseAccessDelegate.get() != null) {
            AudioEventListenerForDatabase listener = new AudioEventListenerForDatabase(databaseAccessDelegate);
            EventPublisher.getInstance().addObserver(listener);
        }



    }

    private DatabaseAccessDelegate createDatabaseAccessDelegate() {
        DatabaseConfigurationDelegate databaseConfigurationDelegate = new DatabaseConfigurationDelegate(() ->
                new DatabaseConfiguration(configurationProvider.get("db"), configurationProvider.get("db_file")));
        databaseConfigurationDelegate.registerSelf(configurationProvider);

        DatabaseDelegate databaseDelegate = new DatabaseDelegate(() -> {
            DatabaseConfiguration configuration = databaseConfigurationDelegate.get();
            if (configuration.getDatabase().equalsIgnoreCase("sqlite")) {
                return new SqliteConnectionController(configuration.getDatabaseFile());
            } else {
                // TODO: Better logging.
                Console.info("Database is not supported: " + configuration.getDatabase());
                return new DisabledOrmLiteConnectionController();
            }
        });

        databaseConfigurationDelegate.addObserver(databaseDelegate);

        DatabaseAccessDelegate databaseAccessDelegate = new DatabaseAccessDelegate(() -> {
            OrmLiteConnectionController controller = databaseDelegate.get();
            if (controller instanceof DisabledOrmLiteConnectionController) {
                return null; // Investigate, is it ok?
            }
            return new DAOHolder(controller);
        });

        databaseDelegate.addObserver(databaseAccessDelegate);
        return databaseAccessDelegate;
    }
}
