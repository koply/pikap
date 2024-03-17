package me.koply.pikap.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.SqliteDatabaseType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.database.model.KnownTrack;
import me.koply.pikap.database.model.RecordedTrack;
import me.koply.pikap.util.Util;

import java.sql.SQLException;
import java.util.Map;

public class SqliteDS implements Database {

    private ConnectionSource connectionSource;

    @Override
    public boolean connect(Map<String, String> config) {
        String dataFilePath = config.getOrDefault("dbfile", "data_" + Util.getDateForFileName() + ".db");

        if (!config.containsKey("dbfile")) {
            Console.log("The dbfile entry couldn't found in the config file. Fallback database file is: " + dataFilePath);
        }

        String connectionUrl = "jdbc:sqlite:" + dataFilePath;

        try {
            connectionSource = new JdbcConnectionSource(connectionUrl, new SqliteDatabaseType());
            initializeDaos(connectionSource);

            Console.log("Database connection established");
            return true;
        } catch (SQLException ex) {
            Console.log("An error occur while connection to the database.");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public void close() {
        try {
            connectionSource.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isFileDB() {
        return true;
    }

    @Override
    public void initializeListeners() {

    }

    // --------- DAOs ----------
    private Dao<KnownTrack, Integer> knownTracks;
    private Dao<RecordedTrack, Integer> recordedTracks;

    private void initializeDaos(ConnectionSource connectionSource) throws SQLException {
        knownTracks = createDaoAndTableIfNotExists(connectionSource, KnownTrack.class);
        recordedTracks = createDaoAndTableIfNotExists(connectionSource, RecordedTrack.class);
    }

    private <Z, Y> Dao<Z, Y> createDaoAndTableIfNotExists(ConnectionSource connectionSource, Class<Z> clazz) throws SQLException {
        Dao<Z, Y> temp = DaoManager.createDao(connectionSource, clazz);
        TableUtils.createTableIfNotExists(connectionSource, clazz);
        return temp;
    }

}