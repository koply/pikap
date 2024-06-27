package me.koply.pikap.util.architechture;

import java.util.function.Supplier;

public class UpdatableDelegate<T> implements Observer, Delegate<T> {

    private final Supplier <T> loadSupplier;

    private volatile T data;

    public UpdatableDelegate(Supplier<T> loadSupplier) {
        this.loadSupplier = loadSupplier;
    }

    @Override
    public synchronized void update() {
        data = loadSupplier.get();
    }

    public synchronized T get() {
        return data;
    }
}
