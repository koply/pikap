package me.koply.pikap.database.branch;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.SqliteDatabaseType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.database.api.Database;
import me.koply.pikap.database.model.*;
import me.koply.pikap.util.Util;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SqliteDB implements Database {

    private ConnectionSource connectionSource;

    private Dao<Track, Integer> tracks;
    private Dao<FavouriteTrack, Integer> favouriteTracks;
    private Dao<RecordedTrack, Integer> recordedTracks;

    private Dao<PlayedPlaylist, Integer> playedPlaylists;
    private Dao<Playlist, Integer> playlists;

    @Override
    public boolean connect(Map<String, String> config) {
        String dataFilePath = config.getOrDefault("dbFile", "data_" + Util.getDateForFileName() + ".db");

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
        playedPlaylists = createDaoAndTableIfNotExists(connectionSource, PlayedPlaylist.class);
        playlists = createDaoAndTableIfNotExists(connectionSource, Playlist.class);
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
    public void updateTrack(Track track) {
        try {
            tracks.update(track);
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
    public List<Track> queryTracksByIds(int[] ids) {
        try {
            return tracks.queryBuilder().where().in("id", (Object) ids).query();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createFavoriteIfNotExists(FavouriteTrack favouriteTrack) {
        _createIfNotExists(favouriteTracks, favouriteTrack, "track", favouriteTrack.getTrack());
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
    public void createPlaylist(Playlist playlist) {
        _createIfNotExists(playlists, playlist, "id", playlist.getId());
    }

    @Override
    public void updatePlaylist(Playlist playlist) {
        try {
            playlists.update(playlist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Playlist queryPlaylistById(int id) {
        try {
            return playlists.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Playlist queryPlaylistByIdentifier(String identifier) {
        try {
            return playlists.queryBuilder().where().eq("youtubeIdentifier", identifier).queryForFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createPlayedPlaylist(PlayedPlaylist playlist) {
        _createIfNotExists(playedPlaylists, playlist, "playlist", playlist.getPlaylist());
    }

    @Override
    public void updatePlayedPlaylist(PlayedPlaylist playlist) {
        try {
            playedPlaylists.update(playlist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayedPlaylist queryPlayedPlaylistById(int id) {
        try {
            return playedPlaylists.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private <T, R> void _createIfNotExists(Dao<T,R> dao, T obj, String columnName, Object value) {
        try {
            T res = dao.queryBuilder().where().eq(columnName, value).queryForFirst();
            if (res == null) {
                dao.create(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}