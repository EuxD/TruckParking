package it.unisannio.ingsw24.Entities.Parking;

public class Parking {

    private String id_park;
    private String address;
    private String city;
    private String id_owner;
    private int numbers;    //posti disponibili
    private Double tariff;

    public Parking(String idpark, String address, String city, String owner, int numbers, Double tariff) {
        this.id_park = idpark;
        this.address = address;
        this.city = city;
        this.id_owner = owner;
        this.numbers = numbers;
        this.tariff = tariff;
    }

    public Parking(String address, String city, String owner, int numbers, Double tariff) {
        this.address = address;
        this.city = city;
        this.id_owner = owner;
        this.numbers = numbers;
        this.tariff = tariff;
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

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public Double getTariff() {
        return tariff;
    }

    public void setTariff(Double tariff) {
        this.tariff = tariff;
    }
}

