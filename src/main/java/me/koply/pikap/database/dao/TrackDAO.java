package me.koply.pikap.database.dao;

import com.j256.ormlite.support.ConnectionSource;
import me.koply.pikap.database.model.Track;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class TrackDAO extends AsyncDataAccessObject<Track>{

    public TrackDAO(ConnectionSource connectionSource, Class<Track> clazz, ExecutorService executorService) {
        super(connectionSource, clazz, executorService);
    }

    public CompletableFuture<Track> fetchTrackByIdentifierAsync(String identifier) {
        return fetchWhereAsync("identifier", identifier);
    }
    public CompletableFuture<Track> fetchLastPlayedAsync() {
        // TODO
        throw new UnsupportedOperationException("Not implemented");
    }
}
