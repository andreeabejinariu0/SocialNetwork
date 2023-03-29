package com.example.labul4.utils.observer;

import com.example.labul4.utils.Event;

import java.sql.SQLException;
/*
* orice obiect care doreste sa fie notificat cand stare altuia se modifica
* */
public interface Observer<E extends Event> {
    void update(E e) throws SQLException;
}
