package me.koply.pikap.util.architecture;

public interface Publisher<T> {
    void subscribe(Subscriber<T> subscriber);
    void unsubscribe(Subscriber<T> subscriber);
    void publish(T t);
}