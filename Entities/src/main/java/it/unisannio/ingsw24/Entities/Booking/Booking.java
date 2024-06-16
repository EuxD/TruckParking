package it.unisannio.ingsw24.Entities.Booking;

import java.util.Date;

public class Booking {

    private String id_booking;
    private String id_trucker;
    private String id_parking;
    private Date pDate;

    public Booking(String idPren,String trucker,String parking,Date pDate){
        this.id_booking=idPren;
        this.id_trucker=trucker;
        this.id_parking=parking;
        this.pDate=pDate;
    }

    public Booking(String trucker,String parking,Date pDate){
        this.id_trucker=trucker;
        this.id_parking=parking;
        this.pDate=pDate;
    }

    public String getId_booking() {
        return id_booking;
    }

    public void setId_booking(String id_booking) {
        this.id_booking = id_booking;
    }

    public String getId_trucker() {
        return id_trucker;
    }

    public void setId_trucker(String id_trucker) {
        this.id_trucker = id_trucker;
    }

    public String getId_parking() {
        return id_parking;
    }

    public void setId_parking(String id_parking) {
        this.id_parking = id_parking;
    }

    public Date getpDate() {
        return pDate;
    }

    public void setpDate(Date pDate) {
        this.pDate = pDate;
    }
}

