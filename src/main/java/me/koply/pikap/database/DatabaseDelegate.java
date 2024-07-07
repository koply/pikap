package me.koply.pikap.database;

import me.koply.pikap.database.connection.OrmLiteConnectionController;
import me.koply.pikap.util.architechture.Observable;
import me.koply.pikap.util.architechture.Observer;
import me.koply.pikap.util.architechture.ObserverDelegate;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Supplier;

public class DatabaseDelegate extends ObserverDelegate<OrmLiteConnectionController> implements Observable {

    public DatabaseDelegate(Supplier<OrmLiteConnectionController> loadSupplier) {
        super(loadSupplier);
    }

    private final Set<Observer> observers = new CopyOnWriteArraySet<>();

    @Override
    public synchronized void update() {
        super.update();
        observers.forEach(Observer::update);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}
