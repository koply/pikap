package me.koply.pikap.util;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

// if stack is full
// overwrites last index
public class FixedStack<T> implements Iterable<T> {

    private final Object[] array;
    public final int size;

    private int dataCount = 0;
    private int cursor;

    public FixedStack(int size) {
        this.size = size;
        this.array = new Object[size];
        this.cursor = 0;
    }

    public void push(T object) {
        array[cursor] = object;
        dataCount++;
        if (cursor+1==size) cursor = 0;
        else cursor++;
    }

    public void addAll(T...arr) {
        for (T obj : arr) {
            push(obj);
        }
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (cursor == 0) cursor = size-1;
        else cursor--;
        dataCount--;
        Object toreturn = array[cursor];
        array[cursor] = null;
        return (T) toreturn;
    }

    @Override
    public Iterator<T> iterator() {
        // final wrapper for anonymous class
        final int lastCursor = cursor;
        return new Iterator<>() {
            int _cursor = lastCursor;
            int i = 0;
            @Override
            public boolean hasNext() {
                if (i==size) return false;
                i++;
                if (_cursor==0) _cursor = size-1;
                else _cursor--;
                return array[_cursor] != null;
            }

            @SuppressWarnings("unchecked")
            @Override
            public T next() {
                return (T) array[_cursor];
            }
        };
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return Iterable.super.spliterator();
    }

    public int getDataCount() {
        return dataCount;
    }
}