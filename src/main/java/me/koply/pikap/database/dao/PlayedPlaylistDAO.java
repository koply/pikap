package me.koply.pikap.database.dao;

import me.koply.pikap.database.connection.OrmLiteConnectionController;
import me.koply.pikap.database.model.PlayedPlaylist;

import java.util.concurrent.ExecutorService;

public class PlayedPlaylistDAO extends AsyncDataAccessObjectOrmLite<PlayedPlaylist> {

    public PlayedPlaylistDAO(OrmLiteConnectionController connectionController, Class<PlayedPlaylist> clazz, ExecutorService executorService) {
        super(connectionController, clazz, executorService);
    }

}
