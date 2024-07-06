package me.koply.pikap.database.connection;

import com.j256.ormlite.support.ConnectionSource;

public interface OrmLiteConnectionController extends ConnectionController<ConnectionSource> {

    ConnectionSource getConnection();
}
