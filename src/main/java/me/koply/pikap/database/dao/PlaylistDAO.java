package me.koply.pikap.database.dao;

import me.koply.pikap.database.connection.OrmLiteConnectionController;
import me.koply.pikap.database.model.Playlist;

import java.util.concurrent.ExecutorService;

public class PlaylistDAO extends AsyncDataAccessObjectOrmLite<Playlist>{

    public PlaylistDAO(OrmLiteConnectionController connectionController, Class<Playlist> clazz, ExecutorService executorService) {
        super(connectionController, clazz, executorService);
    }

}
