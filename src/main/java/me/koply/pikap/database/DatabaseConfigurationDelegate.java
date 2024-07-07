package me.koply.pikap.database;

import me.koply.pikap.config.ConfigDelegate;
import me.koply.pikap.util.architechture.Observable;
import me.koply.pikap.util.architechture.Observer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Supplier;

public class DatabaseConfigurationDelegate extends ConfigDelegate<DatabaseConfiguration> implements Observable {

    public DatabaseConfigurationDelegate(Supplier<DatabaseConfiguration> loadSupplier) {
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
