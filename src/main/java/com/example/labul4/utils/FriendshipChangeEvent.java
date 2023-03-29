package com.example.labul4.utils;

import com.example.labul4.domain.Friendship;

public class FriendshipChangeEvent implements Event {
    private ChangeEventType type;
    private Friendship data, oldData;

    public FriendshipChangeEvent(ChangeEventType type, Friendship friendship){
        this.type = type;
        this.data = friendship;
    }

    public ChangeEventType getType(){
        return this.type;
    }

    public Friendship getData() {
        return this.data;
    }

    public Friendship getOldData() {
        return this.oldData;
    }
}
