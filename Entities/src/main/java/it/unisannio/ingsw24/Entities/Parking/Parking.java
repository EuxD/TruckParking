package it.unisannio.ingsw24.Entities.Parking;

public class Parking {

    private String id_parking;
    private String address;
    private String city;
    private String id_owner;
    private int nPlace;    //posti disponibili
    private Double tariffa;

    public Parking() {}

    public Parking(String idpark, String address, String city, String owner, int nPlace, Double tariffa) {
        this.id_parking = idpark;
        this.address = address;
        this.city = city;
        this.id_owner = owner;
        this.nPlace = nPlace;
        this.tariffa = tariffa;
    }

    public Parking(String address, String city, String owner, int nPlace, Double tariffa) {
        this.address = address;
        this.city = city;
        this.id_owner = owner;
        this.nPlace = nPlace;
        this.tariffa = tariffa;
    }

    public String getId_parking() {
        return id_parking;
    }

    public void setId_parking(String id_parking) {
        this.id_parking = id_parking;
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

    public Double getTariffa() {
        return tariffa;
    }

    public void setTariffa(Double tariffa) {
        this.tariffa = tariffa;
    }

    @Override
    public String toString() {
        return "Parking{" +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", id_owner='" + id_owner + '\'' +
                ", nPlace=" + nPlace +
                ", tariffa=" + tariffa +
                '}';
    }
}

