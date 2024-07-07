package me.koply.pikap.database.connection;

import com.j256.ormlite.support.ConnectionSource;

public class DisabledOrmLiteConnectionController implements OrmLiteConnectionController {
    @Override
    public ConnectionSource getConnection() {
        return null;
    }

    @Override
    public void close(){
    }
}
