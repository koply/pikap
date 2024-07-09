package me.koply.pikap.database.ormlite;

import com.j256.ormlite.support.ConnectionSource;
import lombok.Getter;
import me.koply.pikap.database.connection.ConnectionController;
import me.koply.pikap.database.dao.AsyncTableAccessObject;
import me.koply.pikap.database.ormlite.table.OrmliteTableDAO;
import me.koply.pikap.database.ormlite.table.PlaylistDAO;
import me.koply.pikap.database.ormlite.table.TrackDAO;
import me.koply.pikap.database.model.*;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class DAOHolder {

    private final Map<Class<?>, AsyncTableAccessObject<?>> daos = new ConcurrentHashMap<>();

    private final ExecutorService databaseExecutor;
    private final ConnectionController<?> controller;

    public DAOHolder(ConnectionController<?> controller) {
        this(controller, Executors.newSingleThreadExecutor());
    }

    public DAOHolder(ConnectionController<?> controller, ExecutorService databaseExecutor) {
        this.databaseExecutor = Objects.requireNonNull(databaseExecutor, "Executor cannot be null");
        this.controller = Objects.requireNonNull(controller, "ConnectionController cannot be null");

        daos.put(FavouriteTrack.class, new OrmliteTableDAO<>(FavouriteTrack.class, (ConnectionController<ConnectionSource>) controller, databaseExecutor) {
        });
        favouriteTrackDAO = new FavouriteTrackDAO(controller, FavouriteTrack.class, databaseExecutor);
        playedPlaylistDAO = new PlayedPlaylistDAO(controller, PlayedPlaylist.class, databaseExecutor);
        playlistDAO = new PlaylistDAO(controller, Playlist.class, databaseExecutor);
        recordedTrackDAO = new RecordedTrackDAO(controller, RecordedTrack.class, databaseExecutor);
        trackDAO = new TrackDAO(controller, Track.class, databaseExecutor);
    }

}
