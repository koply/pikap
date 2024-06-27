package me.koply.pikap.event;

import me.koply.pikap.util.architechture.Observer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class ListenerList {

    private volatile List<EventReflectionData> listeners = new ArrayList<>();

    public List<EventReflectionData> getSortedListeners() {
        return listeners;
    }

    public synchronized void add(EventReflectionData data) {
        List<EventReflectionData> copy = new ArrayList<>(listeners);
        copy.add(data);
        copy.sort(Comparator.comparingInt(EventReflectionData::getPriority));
        listeners = copy;
    }

    public synchronized void removeListener(Observer observer) {
        List<EventReflectionData> copy = new ArrayList<>(listeners);
        copy.removeIf(data -> data.getListener() == observer);
        copy.sort(Comparator.comparingInt(EventReflectionData::getPriority));
        listeners = copy;
    }
}
