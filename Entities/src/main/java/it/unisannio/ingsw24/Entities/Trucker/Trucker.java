package it.unisannio.ingsw24.Entities.Trucker;


import it.unisannio.ingsw24.Entities.Persona;

import java.util.Date;
import java.util.List;

public class Trucker extends Persona {

    private String id_trucker;
    private List<String> bookings;

    public Trucker(String id,String name, String surname, Date bDate, String mail, String gender, String role, String password ,List<String> bookings) {
        super(name, surname, bDate, mail, gender, role, password);
        this.id_trucker = id;
        this.bookings = bookings;
    }

    public Trucker(String name, String surname, Date bDate, String mail, String gender, String role, String password ,List<String> bookings) {
        super(name, surname, bDate, mail, gender, role, password);
        this.bookings = bookings;
    }

    public String getId_trucker() {
        return id_trucker;
    }

    public void setId_trucker(String id_trucker) {
        this.id_trucker = id_trucker;
    }

    public List<String> getBookings() {
        return bookings;
    }

    public void setBookings(List<String> bookings) {
        this.bookings = bookings;
    }

}
