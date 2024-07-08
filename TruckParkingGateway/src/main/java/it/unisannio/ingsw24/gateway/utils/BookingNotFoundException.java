package it.unisannio.ingsw24.gateway.utils;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(String msg) {
        super(msg);
    }
}
