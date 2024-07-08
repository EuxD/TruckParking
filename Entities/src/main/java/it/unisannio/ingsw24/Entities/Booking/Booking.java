package it.unisannio.ingsw24.Entities.Booking;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Booking {

    private String id_booking;
    private String id_trucker;
    private String id_parking;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime ora_inizio;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime ora_fine;
    private Double total;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate pDate;

    public Booking(String idPren,String trucker,String parking,LocalDate pDate, LocalTime ora_inizio, LocalTime ora_fine, Double total){
        this.id_booking=idPren;
        this.id_trucker=trucker;
        this.id_parking=parking;
        this.pDate=pDate;
        this.ora_inizio=ora_inizio;
        this.ora_fine=ora_fine;
        this.total=total;
    }

    public Booking(String trucker,String parking,LocalDate pDate, LocalTime ora_inizio, LocalTime ora_fine, Double total){
        this.id_trucker=trucker;
        this.id_parking=parking;
        this.pDate=pDate;
        this.ora_inizio=ora_inizio;
        this.ora_fine=ora_fine;
        this.total=total;
    }

    public Booking(){

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

    public LocalDate getpDate() {
        return pDate;
    }

    public void setpDate(LocalDate pDate) {
        this.pDate = pDate;
    }

    public LocalTime getOra_inizio() {
        return ora_inizio;
    }

    public void setOra_inizio(LocalTime ora_inizio) {
        this.ora_inizio = ora_inizio;
    }

    public LocalTime getOra_fine() {
        return ora_fine;
    }

    public void setOra_fine(LocalTime ora_fine) {
        this.ora_fine = ora_fine;
    }

    public Double getTotal() {return this.total;}



    public void setTotal(Double total) {this.total = total;}

}

