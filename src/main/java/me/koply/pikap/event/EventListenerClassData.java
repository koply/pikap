package me.koply.pikap.event;

import java.lang.reflect.Method;

public class EventListenerClassData {

    public final Method method;
    public final Object listener;

    public EventListenerClassData(Method method, Object listener) {
        this.method = method;
        this.listener = listener;
    }
}