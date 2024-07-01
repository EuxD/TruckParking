package it.unisannio.ingsw24.gateway.logic;

import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Parking.Parking;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;

import java.io.IOException;
import java.util.List;

public interface GatewayLogic {

    Owner createOwner(Owner owner) throws IOException;
    Boolean deleteOwnerByEmail(String email) throws IOException;
    Owner getOwnerByEmail(String email) throws IOException;
    Owner getOwnerById(String id) throws IOException;
    Boolean deleteOwnerByID(String id) throws IOException;
    Owner updateOwner(Owner owner) throws IOException;

    Trucker createTrucker(Trucker trucker) throws IOException;
    Boolean deleteTruckerByEmail(String email);
    Boolean deleteTruckerByID(String id);
    Trucker getTruckerByEmail(String email);
    Trucker getTruckerByID(String id);
    Trucker updateTrucker(Trucker trucker) throws IOException;

    String parkingAddress = "http://localhost:8083";
    Parking createParking(Parking parking) throws IOException;
    Parking getParkingById(String id);
    Boolean deleteParkingById(String id);
    List<Parking> getParkingByIdOwner(String id);
    List<Parking> getAllParking();
    Boolean updateParking(Parking parking) throws IOException;
}
