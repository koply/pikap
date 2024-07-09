package me.koply.pikap.database.ormlite.table;

import com.j256.ormlite.support.ConnectionSource;
import me.koply.pikap.database.connection.ConnectionController;
import me.koply.pikap.database.model.Playlist;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class PlaylistDAO extends OrmliteTableDAO<Playlist> {

    public PlaylistDAO(ConnectionController<ConnectionSource> connectionController, Class<Playlist> clazz, ExecutorService executorService) {
        super(connectionController, clazz, executorService);
    }

    public CompletableFuture<Playlist> fetchByIdentifierAsync(String identifier) {
        return super.fetchWhereAsync("identifier", identifier);
    }
}
