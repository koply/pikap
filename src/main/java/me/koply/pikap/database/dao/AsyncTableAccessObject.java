package me.koply.pikap.database.dao;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @param <T> The object that persists in this DAO
 */
public interface AsyncTableAccessObject<T> {
    CompletableFuture<Void> insertAsync(T t);
    CompletableFuture<Void> upsertAsync(T t);
    CompletableFuture<Void> updateAsync(T t);
    CompletableFuture<Void> deleteAsync(T t);
    CompletableFuture<T> fetchWhereAsync(String column, Object object);
    CompletableFuture<T> fetchAsync(int id);
    CompletableFuture<List<T>> fetchAllAsync();
    CompletableFuture<List<T>> fetchMultipleAsync(Collection<Integer> ids);
}
