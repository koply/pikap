package me.koply.pikap.database.dao;

public interface DatabaseService {
    DatabaseAccessor getAccessor();
    void init();
    void close();
}
