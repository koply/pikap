package me.koply.pikap.database.connection;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.SqliteDatabaseType;
import com.j256.ormlite.support.ConnectionSource;
import me.koply.pikap.api.cli.Console;

import java.sql.SQLException;

public class SqliteConnectionController implements ConnectionController {

    private final String connectionUrl ;
    private ConnectionSource connectionSource;

    public SqliteConnectionController(String databaseFilePath) {
        this.connectionUrl = "jdbc:sqlite:" + databaseFilePath;
    }

    @Override
    public ConnectionSource getConnection() {
        if(connectionSource != null) return connectionSource;

        try {
            connectionSource = new JdbcConnectionSource(connectionUrl, new SqliteDatabaseType());
            return connectionSource;
        } catch (SQLException e) {
            // TODO BETTER LOGGING
            connectionSource = null;
            Console.debugLog("Failed to connect to the database.");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() {
        if(connectionSource == null) return;
        try {
            connectionSource.close();
            connectionSource = null;
        } catch (Exception e) {
            // TODO BETTER LOGGING
            Console.debugLog("Failed to close the database connection.");
        }
    }
}
