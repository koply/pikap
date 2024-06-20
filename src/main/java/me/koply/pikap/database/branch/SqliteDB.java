package me.koply.pikap.database.branch;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.SqliteDatabaseType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.database.api.Database;
import me.koply.pikap.database.model.FavouriteTrack;
import me.koply.pikap.database.model.Track;
import me.koply.pikap.database.model.RecordedTrack;
import me.koply.pikap.util.Util;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SqliteDB implements Database {

    private ConnectionSource connectionSource;

    private Dao<Track, Integer> tracks;
    private Dao<FavouriteTrack, Integer> favouriteTracks;
    private Dao<RecordedTrack, Integer> recordedTracks;

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

    private void initializeDaos(ConnectionSource connectionSource) throws SQLException {
        tracks = createDaoAndTableIfNotExists(connectionSource, Track.class);
        favouriteTracks = createDaoAndTableIfNotExists(connectionSource, FavouriteTrack.class);
        recordedTracks = createDaoAndTableIfNotExists(connectionSource, RecordedTrack.class);
    }

    private <Z, Y> Dao<Z, Y> createDaoAndTableIfNotExists(ConnectionSource connectionSource, Class<Z> clazz) throws SQLException {
        Dao<Z, Y> temp = DaoManager.createDao(connectionSource, clazz);
        TableUtils.createTableIfNotExists(connectionSource, clazz);
        return temp;
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


    // Database API

    @Override
    public void createTrack(Track track) {
        try {
            tracks.create(track);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTrackIfNotExists(Track track) {
        try {
            if (tracks.queryBuilder().where().eq("identifier", track.getIdentifier()).countOf() == 0) {
                tracks.create(track);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTrack(Track track) {
        try {
            tracks.delete(track);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Track queryTrackById(int id) {
        try {
            return tracks.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Track queryTrackByIdentifier(String identifier) {
        try {
            return tracks.queryBuilder().where().eq("identifier", identifier).queryForFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Track> queryAllTracks() {
        try {
            return tracks.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createFavoriteIfNotExists(FavouriteTrack favouriteTrack) {
        try {
            favouriteTracks.createIfNotExists(favouriteTrack);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFavorite(FavouriteTrack favouriteTrack) {
        try {
            favouriteTracks.delete(favouriteTrack);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FavouriteTrack> queryAllFavorites() {
        try {
            return favouriteTracks.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initializeListeners() {

    }

}