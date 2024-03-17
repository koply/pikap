package me.koply.pikap.event;

import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.event.AudioEvent;
import me.koply.pikap.api.event.EventListenerAdapter;
import me.koply.pikap.api.event.PlayEvent;
import org.reflections8.Reflections;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// internal event methods
public class EventManager {

    public static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    // schema: Class<? extends PlayerListenerAdapter>
    private static final Map<Class<? extends AudioEvent>, Set<EventListenerData>> listeners = new HashMap<>();
    static {
        var reflections = new Reflections(AudioEvent.class.getPackage().getName());
        var classes = reflections.getSubTypesOf(AudioEvent.class);
        for (var clazz : classes) {
            var x = new HashSet<EventListenerData>();
            listeners.put(clazz, x);
        }
    }

    public static void registerListener(Object listener) {
        if (listener == null) throw new IllegalArgumentException("The listener cannot be null.");
        if (!(listener instanceof EventListenerAdapter)) throw new IllegalArgumentException("The listener must be a PlayerListenerAdapter");
        Class<? extends EventListenerAdapter> clazz = listener.getClass().asSubclass(EventListenerAdapter.class);
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            Parameter parameter = method.getParameters()[0];
            if (listeners.containsKey(parameter.getType())) {
                listeners.get(parameter.getType()).add(new EventListenerData(method, listener));
            }
        }
    }

    // works asynchronously
    public static void pushEvent(Object eventObject) {
        if (eventObject == null) throw new IllegalArgumentException("The event cannot be null.");
        if (!(eventObject instanceof AudioEvent)) throw new IllegalArgumentException("eventObject must be an AudioEvent instance.");
        if (listeners.containsKey(eventObject.getClass())) {
            EXECUTOR_SERVICE.submit(() -> {
                try {
                    for (var data : listeners.get(eventObject.getClass())) {
                        data.method.invoke(data.listener, eventObject);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    public static void debugListeners() {
        Console.info(listeners.size()  + " (listeners size) - " + listeners.get(PlayEvent.class).size() + " (total PlayEvent listener count) EventManager#debugListeners()");
    }
}