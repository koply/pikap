package me.koply.pikap.database;

import me.koply.pikap.model.PastTrack;

import java.util.List;
import java.util.Map;

public interface DBRepository {

    boolean connect(Map<String, String> config);
    void initializeListeners();
    void pushHistory(PastTrack track);
    List<PastTrack> retrieveHistory();
    boolean isFileDB();

}