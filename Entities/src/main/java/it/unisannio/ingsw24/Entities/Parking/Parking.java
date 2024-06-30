package it.unisannio.ingsw24.Entities.Parking;

public class Parking {

    private String id_park;
    private String address;
    private String city;
    private String id_owner;
    private int nPlace;    //posti disponibili
    private Double rate;

    public Parking() {}

    public Parking(String idpark, String address, String city, String owner, int nPlace, Double rate) {
        this.id_park = idpark;
        this.address = address;
        this.city = city;
        this.id_owner = owner;
        this.nPlace = nPlace;
        this.rate = rate;
    }

    public Parking(String address, String city, String owner, int nPlace, Double rate) {
        this.address = address;
        this.city = city;
        this.id_owner = owner;
        this.nPlace = nPlace;
        this.rate = rate;
    }

    public String getId_park() {
        return id_park;
    }

    public void setId_park(String id_park) {
        this.id_park = id_park;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId_owner() {
        return id_owner;
    }

    public void setId_owner(String id_owner) {
        this.id_owner = id_owner;
    }

    public int getnPlace() {
        return nPlace;
    }

    public void setnPlace(int nPlace) {
        this.nPlace = nPlace;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Parking{" +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", id_owner='" + id_owner + '\'' +
                ", nPlace=" + nPlace +
                ", rate=" + rate +
                '}';
    }
}

