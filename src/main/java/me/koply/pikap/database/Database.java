package me.koply.pikap.database;

import java.util.Map;

public interface Database {

    boolean connect(Map<String, String> config);
    void close();
    boolean isFileDB();

    void initializeListeners();

}