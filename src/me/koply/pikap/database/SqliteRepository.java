package me.koply.pikap.database;

import me.koply.pikap.model.PastTrack;

import java.util.List;
import java.util.Map;

public class SqliteRepository implements DBRepository {

    // TODO write the methods

    @Override
    public boolean connect(Map<String, String> config) {
        return false;
    }

    @Override
    public void initializeListeners() {

    }

    @Override
    public void pushHistory(PastTrack track) {

    }

    @Override
    public List<PastTrack> retrieveHistory() {
        return null;
    }

    @Override
    public boolean isFileDB() {
        return false;
    }
}