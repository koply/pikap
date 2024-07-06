package me.koply.pikap.database.connection;

/**
 * @param <T> ConnectionSource
 */
public interface ConnectionController<T> extends AutoCloseable {

    T getConnection();
}
