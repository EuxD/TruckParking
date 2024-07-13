package it.unisannio.ingsw24.booking.utils;

public class BookingNotExpiredException extends RuntimeException{
    public BookingNotExpiredException(String msg){
        super(msg);
    }
}
