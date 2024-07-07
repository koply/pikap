package me.koply.pikap.database.model;

import lombok.Getter;
import me.koply.pikap.database.connection.OrmLiteConnectionController;
import me.koply.pikap.database.dao.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class DatabaseAccessor {

    private final FavouriteTrackDAO favouriteTrackDAO;
    private final PlayedPlaylistDAO playedPlaylistDAO;
    private final PlaylistDAO playlistDAO;
    private final RecordedTrackDAO recordedTrackDAO;
    private final TrackDAO trackDAO;

    private final ExecutorService databaseExecutor;

    public DatabaseAccessor(OrmLiteConnectionController controller) {
        this(controller, Executors.newSingleThreadExecutor());
    }

    public DatabaseAccessor(OrmLiteConnectionController controller, ExecutorService databaseExecutor) {
        this.databaseExecutor = databaseExecutor;

        favouriteTrackDAO = new FavouriteTrackDAO(controller, FavouriteTrack.class, databaseExecutor);
        playedPlaylistDAO = new PlayedPlaylistDAO(controller, PlayedPlaylist.class, databaseExecutor);
        playlistDAO = new PlaylistDAO(controller, Playlist.class, databaseExecutor);
        recordedTrackDAO = new RecordedTrackDAO(controller, RecordedTrack.class, databaseExecutor);
        trackDAO = new TrackDAO(controller, Track.class, databaseExecutor);
    }

}
