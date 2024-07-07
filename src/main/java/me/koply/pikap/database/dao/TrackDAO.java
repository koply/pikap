package me.koply.pikap.database.dao;

import me.koply.pikap.database.connection.OrmLiteConnectionController;
import me.koply.pikap.database.model.Track;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class TrackDAO extends AsyncDataAccessObjectOrmLite<Track> {

    public TrackDAO(OrmLiteConnectionController connectionController, Class<Track> clazz, ExecutorService executorService) {
        super(connectionController, clazz, executorService);
    }

    public CompletableFuture<Track> fetchTrackByIdentifierAsync(String identifier) {
        return fetchWhereAsync("identifier", identifier);
    }

    public CompletableFuture<Track> fetchLastPlayedAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                long max = dao.queryRawValue("select max(lastPlayed)");
                return dao.queryBuilder().where().eq("lastPlayed", max).queryForFirst();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }
}
