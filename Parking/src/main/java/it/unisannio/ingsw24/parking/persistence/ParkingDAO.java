package it.unisannio.ingsw24.parking.persistence;

import it.unisannio.ingsw24.Entities.Booking.Booking;
import it.unisannio.ingsw24.Entities.Parking.Parking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface ParkingDAO {

    String DATABASE_NAME = "TruckParking";
    String COLLECTION_NAME = "Parking";
    String ELEMENT_ID = "id_parking";
    String ELEMENT_ADDRESS = "address";
    String ELEMENT_CITY = "city";
    String ELEMENT_ID_OWNER = "id_owner";
    String ELEMENT_PLACES = "nPlace";
    String ELEMENT_TARIFFA = "tariffa";

    Parking createParking(Parking parking);
    Parking findParkingById(String id);
    Boolean deleteParkingById(String id) throws IOException;
    List<Parking> findParkingByIdOwner(String id);
    List<Parking> getAllParking();
    Boolean updateParking(String id, Parking parking);
    List<Parking> getParkingByCity(String city);

    List<Booking> getBookingByParking(String idOwner);
}
