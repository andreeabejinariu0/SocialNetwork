package com.example.labul4.utils.observer;

import com.example.labul4.utils.Event;
/*
* orice obiect a carui stare poate fi de interes
* */
public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}