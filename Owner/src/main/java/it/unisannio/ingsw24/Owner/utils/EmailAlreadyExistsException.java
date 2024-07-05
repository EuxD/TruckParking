package it.unisannio.ingsw24.Owner.utils;

public class EmailAlreadyExistsException extends RuntimeException{

    public  EmailAlreadyExistsException(String msg){
        super(msg);
    }
}
