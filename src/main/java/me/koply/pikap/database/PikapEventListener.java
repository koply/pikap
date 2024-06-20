package me.koply.pikap.database;

import me.koply.pikap.api.event.EventListenerAdapter;
import me.koply.pikap.api.event.PlayEvent;
import me.koply.pikap.database.api.Database;
import me.koply.pikap.database.model.Track;

public class PikapEventListener extends EventListenerAdapter {

    private final Database db;
    public PikapEventListener(Database db) {
        this.db = db;
    }

    @Override
    public void onPlay(PlayEvent e) {
        db.createTrackIfNotExists(new Track(e.track.getInfo()));
    }
}
