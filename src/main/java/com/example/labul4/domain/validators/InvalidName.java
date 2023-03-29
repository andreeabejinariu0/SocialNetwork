package com.example.labul4.domain.validators;

public class InvalidName extends Exception{
    InvalidName(){

    }
    public InvalidName(String message){
        super(message);
    }
}
