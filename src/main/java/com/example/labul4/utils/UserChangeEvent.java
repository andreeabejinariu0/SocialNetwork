package com.example.labul4.utils;

import com.example.labul4.domain.User;

public class UserChangeEvent implements Event{
    private ChangeEventType eventType;
    private User data, oldData;

    /**
     * Constructor pentru UserChangeEvent.
     * @param type tipul evenimentului
     * @param data datele noi
     */
    public UserChangeEvent(ChangeEventType type, User data){
        this.eventType = type;
        this.data = data;
    }

    /**
     * Constructor pentru UserChangeEvent.
     *@param type tipul evenimentului
     *@param data datele noi
     * @param oldData datele vechi
     */
    public UserChangeEvent(ChangeEventType type, User data, User oldData){
        this.eventType = type;
        this.data = data;
        this.oldData = oldData;
    }

    /**
     * Getter pentru tipul evenimentului.
     * @return tipul evenimentului
     */
    public ChangeEventType getEventType(){return this.eventType;}

    /**
     * Getter pentru datele user-ului nou.
     * @return datele noi
     */
    public User getData(){return this.data;}
    /**
     * Getter pentru datele user-ului vechi.
     * @return datele vechi
     */
    public User getOldData(){return this.oldData;}
}
