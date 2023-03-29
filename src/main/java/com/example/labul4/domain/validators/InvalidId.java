package com.example.labul4.domain.validators;

public class InvalidId extends Exception{

    InvalidId(){
    }
    public InvalidId(String message){
        super(message);
    }

}
