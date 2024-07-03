package it.unisannio.ingsw24.Entities.Booking;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Booking {

    private String id_booking;
    private String id_trucker;
    private String id_parking;
    @JsonFormat(pattern = "HH:mm")
    private Date ora_inizio;
    @JsonFormat(pattern = "HH:mm")
    private Date ora_fine;
    private Double tariffa;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date pDate;

    public Booking(String idPren,String trucker,String parking,Date pDate, Date ora_inizio, Date ora_fine, Double tariffa){
        this.id_booking=idPren;
        this.id_trucker=trucker;
        this.id_parking=parking;
        this.pDate=pDate;
        this.ora_inizio=ora_inizio;
        this.ora_fine=ora_fine;
        this.tariffa=tariffa;
    }

    public Booking(String trucker,String parking,Date pDate, Date ora_inizio, Date ora_fine, Double tariffa){
        this.id_trucker=trucker;
        this.id_parking=parking;
        this.pDate=pDate;
        this.ora_inizio=ora_inizio;
        this.ora_fine=ora_fine;
        this.tariffa=tariffa;
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

    public Date getpDate() {
        return pDate;
    }

    public void setpDate(Date pDate) {
        this.pDate = pDate;
    }

    public Date getOra_inizio() {
        return this.ora_inizio;
    }

    public Date getOra_fine() {return this.ora_fine;}

    public Double getTariffa() {return this.tariffa;}

    public void setOra_fine(Date ora_fine) {this.ora_fine = ora_fine;}

    public void setOra_inizio(Date ora_inizio) {this.ora_inizio = ora_inizio;}

    public void setTariffa(Double tariffa) {this.tariffa = tariffa;}
}

