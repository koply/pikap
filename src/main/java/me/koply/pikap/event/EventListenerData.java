package me.koply.pikap.event;

import java.lang.reflect.Method;

public class EventListenerData {

    public final Method method;
    public final Object listener;

    public EventListenerData(Method method, Object listener) {
        this.method = method;
        this.listener = listener;
    }
}