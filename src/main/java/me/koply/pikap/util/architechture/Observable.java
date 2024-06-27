package me.koply.pikap.util.architechture;

public interface Observable {

    void addObserver(Observer observer);
    void removeObserver(Observer observer);
}
