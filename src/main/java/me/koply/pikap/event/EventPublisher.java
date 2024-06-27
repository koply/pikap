package me.koply.pikap.event;

import me.koply.pikap.util.architechture.Event;
import me.koply.pikap.util.architechture.Observable;
import me.koply.pikap.util.architechture.Observer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

// internal event methods
public class EventPublisher implements Observable {

    public final Executor executor = Executors.newSingleThreadExecutor();

    private final Map<Class<? extends Event>, ListenerList> registeredListeners = new ConcurrentHashMap<>();

    private static EventPublisher instance;

    public static EventPublisher getInstance() {
        if (instance == null) instance = new EventPublisher();
        return instance;
    }
    @Override
    public void addObserver(Observer observer) {
        Arrays.stream(observer.getClass().getDeclaredMethods())
                .filter( method -> method.isAnnotationPresent(EventHandler.class) )
                .forEach( method -> {
                    EventHandler handler = method.getDeclaredAnnotation(EventHandler.class);

                    Parameter[] parameters = method.getParameters();
                    if (parameters.length != 1) return;

                    Parameter parameter = parameters[0];
                    Class<?> parameterClazz = parameter.getType();

                    if (!Event.class.isAssignableFrom(parameterClazz)) return;

                    ListenerList listeners = getListenerList(parameterClazz);

                    listeners.add(new EventReflectionData(method, observer, handler.priority()));
                });
    }

    @Override
    public void removeObserver(Observer observer) {
        registeredListeners.values().forEach((list) -> list.removeListener(observer));
    }

    public void publishEvent(Event event) {
        if(event == null) return;
        Class<? extends Event> clazz = event.getClass();

        if(!registeredListeners.containsKey(clazz)) return;

        final List<EventReflectionData> sortedListeners = registeredListeners.get(clazz).getSortedListeners();

        executor.execute(() -> sortedListeners.forEach((data) -> {
            try {
                data.getMethod().invoke(data.getListener(), event);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }));
    }

    private ListenerList getListenerList(Class<?> parameterClazz) {
        // lazy register classes.
        ListenerList listeners;
        if (!registeredListeners.containsKey(parameterClazz)) {
            listeners = new ListenerList();
            // should be safe to ignore.
            //noinspection unchecked
            registeredListeners.put((Class<? extends Event>) parameterClazz, listeners);
        } else {
            listeners = registeredListeners.get(parameterClazz);
        }
        return listeners;
    }

    /*

    public static void registerListener(Object listener) {
        if (listener == null) throw new IllegalArgumentException("The listener cannot be null.");
        if (!(listener instanceof AudioEventListenerAdapter)) throw new IllegalArgumentException("The listener must be a PlayerListenerAdapter");
        Class<? extends AudioEventListenerAdapter> clazz = listener.getClass().asSubclass(AudioEventListenerAdapter.class);
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            Parameter parameter = method.getParameters()[0];
            if (listeners.containsKey(parameter.getType())) {
                listeners.get(parameter.getType()).add(new EventListenerClassData(method, listener));
            }
        }
    }

    public static void unregisterListener(Object listener) {
        if (!(listener instanceof AudioEventListenerAdapter)) throw new IllegalArgumentException("The listener must be a PlayerListenerAdapter");
        Class<? extends AudioEventListenerAdapter> clazz = listener.getClass().asSubclass(AudioEventListenerAdapter.class);
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Method[] adaptersMethods = AudioEventListenerAdapter.class.getDeclaredMethods();
        for (Map.Entry<Class<? extends AudioEvent>, Set<EventListenerClassData>> entry : listeners.entrySet()) {
            for (Method method : declaredMethods) {
                for (Method method2 : adaptersMethods) {
                    if (method.getName().equals(method2.getName()) && method.getParameters()[0] == method2.getParameters()[0]) {
                        Set<EventListenerClassData> toRemove = new HashSet<>();
                        for (EventListenerClassData data : entry.getValue()) {
                            if (data.method == method) {
                                toRemove.add(data);
                            }
                        }
                        entry.getValue().removeAll(toRemove);
                    }
                }
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

    */
}
