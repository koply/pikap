package me.koply.pikap.database.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AsyncDataAccessObject<T> {

    protected final ExecutorService executorService;
    protected final Dao<T, Integer> dao;

    public AsyncDataAccessObject(ConnectionSource connectionSource, Class<T> clazz, ExecutorService executorService) {
        if(executorService == null) executorService = Executors.newSingleThreadExecutor();

        this.executorService = executorService;

        Dao<T, Integer> dao = null;

        try {
            dao = DaoManager.createDao(connectionSource, clazz);
            TableUtils.createTableIfNotExists(connectionSource, clazz);
        } catch (SQLException e) {
            //TODO BETTER LOGGING
            e.printStackTrace();
        }

        this.dao = dao;
    }

    public CompletableFuture<Void> insertAsync(T t) {
        return CompletableFuture.runAsync(() -> {
            try {
                dao.create(t);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }

    public CompletableFuture<Void> upsertAsync(T t) {
        return CompletableFuture.runAsync(() -> {
            try {
                dao.createOrUpdate(t);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }

    public CompletableFuture<Void> updateAsync(T t) {
        return CompletableFuture.runAsync(() -> {
            try {
                dao.update(t);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }

    public CompletableFuture<Void> deleteAsync(T t) {
        return CompletableFuture.runAsync(() -> {
            try {
                dao.delete(t);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }

    public CompletableFuture<T> fetchWhereAsync(String column, Object object) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return dao.queryBuilder().where().eq(column, object).queryForFirst();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }

    public CompletableFuture<T> fetchAsync(int id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return dao.queryForId(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }

    public CompletableFuture<List<T>> fetchAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return dao.queryForAll();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }

    public CompletableFuture<List<T>> fetchMultipleAsync(Collection<Integer> ids) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return dao.queryBuilder().where().in("id", (Object) ids).query();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }
}