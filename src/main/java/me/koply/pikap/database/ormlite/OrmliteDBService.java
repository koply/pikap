package me.koply.pikap.database.ormlite;

import me.koply.pikap.database.dao.DatabaseAccessor;
import me.koply.pikap.database.dao.DatabaseService;

public class OrmliteDBService implements DatabaseService {
    @Override
    public DatabaseAccessor getAccessor() {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    public void close() {

    }
}
