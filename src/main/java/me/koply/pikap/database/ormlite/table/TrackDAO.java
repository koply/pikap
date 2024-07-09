package me.koply.pikap.database.ormlite.table;

import com.j256.ormlite.support.ConnectionSource;
import me.koply.pikap.database.connection.ConnectionController;
import me.koply.pikap.database.model.Track;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class TrackDAO extends OrmliteTableDAO<Track> {

    public TrackDAO(ConnectionController<ConnectionSource> connectionController, Class<Track> clazz, ExecutorService executorService) {
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
