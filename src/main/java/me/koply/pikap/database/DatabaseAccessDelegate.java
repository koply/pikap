package me.koply.pikap.database;

import me.koply.pikap.database.model.DatabaseAccessor;
import me.koply.pikap.util.architechture.ObserverDelegate;

import java.util.function.Supplier;

public class DatabaseAccessDelegate extends ObserverDelegate<DatabaseAccessor> {

    public DatabaseAccessDelegate(Supplier<DatabaseAccessor> loadSupplier) {
        super(loadSupplier);
    }

    @Override
    public synchronized void update() {
        super.update();
    }
}
