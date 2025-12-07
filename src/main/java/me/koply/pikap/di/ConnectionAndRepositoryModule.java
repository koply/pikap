package me.koply.pikap.di;

import dagger.Module;
import dagger.Provides;
import me.koply.pikap.database.DatabaseDelegate;
import me.koply.pikap.database.impl.SQLiteTrackRepository;
import me.koply.pikap.database.repository.TrackRepository;
import me.koply.pikap.util.architecture.Delegate;

import javax.inject.Singleton;
import java.sql.Connection;

@Module
public class ConnectionAndRepositoryModule {

    @Provides
    @Singleton
    public Delegate<Connection> provideConnectionDelegate(DatabaseDelegate impl) {
        return impl;
    }

    @Provides
    @Singleton
    public TrackRepository provideTrackRepository(Delegate<Connection> connectionDelegate) {
        return new SQLiteTrackRepository(connectionDelegate);
    }

}
