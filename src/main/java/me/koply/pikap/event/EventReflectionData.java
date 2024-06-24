package me.koply.pikap.event;

import java.lang.reflect.Method;

class EventReflectionData {

    private final Method method;
    private final Object listener;
    private final int priority;

    public EventReflectionData(Method method, Object listener, int priority) {
        this.method = method;
        this.listener = listener;
        this.priority = priority;
    }

    public Method getMethod() {
        return method;
    }

    public Object getListener() {
        return listener;
    }

    public int getPriority() {
        return priority;
    }
}