package it.unisannio.ingsw24.Entities.Payment;

import java.util.Date;

public class Payment {


    private String id_payment;
    private Date start;
    private Date end;
    private String id_parking;
    private Double total;

    public Payment(String id_payment, Date start, Date end, String parking) {
        this.id_payment = id_payment;
        this.start = start;
        this.end = end;
        this.id_parking = parking;
    }

    public String getId_payment() {
        return id_payment;
    }

    public void setId_payment(String id_payment) {
        this.id_payment = id_payment;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getId_parking() {
        return id_parking;
    }

    public void setId_parking(String id_parking) {
        this.id_parking = id_parking;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}

