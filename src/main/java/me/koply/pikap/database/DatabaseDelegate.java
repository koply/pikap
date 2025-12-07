package me.koply.pikap.database;

import lombok.extern.slf4j.Slf4j;
import me.koply.pikap.config.Configuration;
import me.koply.pikap.config.ConfigurationDelegate;
import me.koply.pikap.util.architecture.Delegate;
import me.koply.pikap.util.architecture.Subscriber;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Singleton
public class DatabaseDelegate implements Delegate<Connection>, Subscriber<Configuration> {

    private final AtomicReference<Connection> connectionReference = new AtomicReference<>();

    private final ConfigurationDelegate configurationDelegate;

    @Inject
    public DatabaseDelegate(ConfigurationDelegate configurationDelegate) {
        this.configurationDelegate = configurationDelegate;

        if (configurationDelegate.get() != null) {
            this.accept(configurationDelegate.get());
        }
    }

    @Override
    public Connection get() {
        Connection current = connectionReference.get();
        try {
            if (current == null || current.isClosed()) {
                accept(configurationDelegate.get());
                return connectionReference.get();
            }
        } catch (SQLException e) {
            log.error("Failed to get database connection", e);
        }
        return connectionReference.get();
    }

    @Override
    public synchronized void accept(Configuration configuration) {
        try {
            Connection oldConnection = connectionReference.get();

            Connection newConnection = createConnection(configuration);
            connectionReference.set(newConnection);
            log.info("Database connection refreshed for: {}", configuration.getDatabaseBackend());

            if (oldConnection != null && !oldConnection.isClosed()) {
                oldConnection.close();
            }
        } catch (SQLException e) {
            log.error("Failed to refresh database connection", e);
        }
    }

    private Connection createConnection(Configuration configuration) throws SQLException {
        if ("sqlite".equalsIgnoreCase(configuration.getDatabaseBackend())) {
            return DriverManager.getConnection("jdbc:sqlite:" + configuration.getDatabaseFilePath());
        } else if ("mysql".equalsIgnoreCase(configuration.getDatabaseBackend())) {
            return DriverManager.getConnection("jdbc:mysql://...", configuration.getDatabaseUsername(), configuration.getDatabasePassword());
        }
        throw new SQLException("Unsupported database type: " + configuration.getDatabaseBackend());
    }


    @Override
    public Class<Configuration> getType() {
        return Configuration.class;
    }
}
