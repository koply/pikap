package me.koply.pikap.database.dao;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * @param <T> The object that persists in this DAO
 */
public abstract class AsyncDataAccessObject<T> {

    protected final ExecutorService executorService;

    public AsyncDataAccessObject(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public abstract CompletableFuture<Void> insertAsync(T t);
    public abstract CompletableFuture<Void> upsertAsync(T t);
    public abstract CompletableFuture<Void> updateAsync(T t);
    public abstract CompletableFuture<Void> deleteAsync(T t);
    public abstract CompletableFuture<T> fetchWhereAsync(String column, Object object);
    public abstract CompletableFuture<T> fetchAsync(int id);
    public abstract CompletableFuture<List<T>> fetchAllAsync();
    public abstract CompletableFuture<List<T>> fetchMultipleAsync(Collection<Integer> ids);

}
