package com.example.banking_system.domain.exception;

public class InvalidAmountException extends RuntimeException{

    public  InvalidAmountException(String message){

        super(message);
    }

}
