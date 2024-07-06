package me.koply.pikap.database.dao;

import me.koply.pikap.database.connection.OrmLiteConnectionController;
import me.koply.pikap.database.model.FavouriteTrack;

import java.util.concurrent.ExecutorService;

public class FavouriteTrackDAO extends AsyncDataAccessObjectOrmLite<FavouriteTrack> {

    public FavouriteTrackDAO(OrmLiteConnectionController connectionController, Class<FavouriteTrack> clazz, ExecutorService executorService) {
        super(connectionController, clazz, executorService);
    }

}
