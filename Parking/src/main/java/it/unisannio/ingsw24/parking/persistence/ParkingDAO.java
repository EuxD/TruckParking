package it.unisannio.ingsw24.parking.persistence;

import it.unisannio.ingsw24.Entities.Parking.Parking;

public interface ParkingDAO {

    String DATABASE_NAME = "TruckParking";
    String COLLECTION_NAME = "Parking";
    String ELEMENT_ID = "id_park";
    String ELEMENT_ADDRESS = "address";
    String ELEMENT_CITY = "city";
    String ELEMENT_ID_OWNER = "id_owner";
    String ELEMENT_PLACES = "nPlace";
    String ELEMENT_RATE = "rate";

    Parking createParking(Parking parking);
    Parking findParkingById(String id);
    Boolean deleteParkingById(String id);

}
