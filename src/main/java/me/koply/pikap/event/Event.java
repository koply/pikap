package me.koply.pikap.event;

public interface Event {
    boolean isCancellable();
    boolean isCanceled();
}
