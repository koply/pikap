package me.koply.pikap.database.dao;

import me.koply.pikap.database.connection.OrmLiteConnectionController;
import me.koply.pikap.database.model.RecordedTrack;

import java.util.concurrent.ExecutorService;

public class RecordedTrackDAO extends AsyncDataAccessObjectOrmLite<RecordedTrack> {
    public RecordedTrackDAO(OrmLiteConnectionController connectionController, Class<RecordedTrack> clazz, ExecutorService executorService) {
        super(connectionController, clazz, executorService);
    }
}
