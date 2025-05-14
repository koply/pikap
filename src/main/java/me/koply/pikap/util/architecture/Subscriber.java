package me.koply.pikap.util.architecture;

public interface Subscriber<T> {
    void accept(T t);
    Class<T> getType();
}