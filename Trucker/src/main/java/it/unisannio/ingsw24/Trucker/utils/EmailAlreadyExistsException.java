package it.unisannio.ingsw24.Trucker.utils;

public class EmailAlreadyExistsException extends RuntimeException{

    public EmailAlreadyExistsException(String msg) {
        super(msg);
    }
}
