package me.koply.pikap.util.architechture;

public interface Event {
    boolean isCancellable();
    boolean isCanceled();
}
