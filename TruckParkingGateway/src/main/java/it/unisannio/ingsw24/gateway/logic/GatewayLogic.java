package it.unisannio.ingsw24.gateway.logic;

import it.unisannio.ingsw24.Entities.Booking.Booking;
import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Parking.Parking;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import org.apache.el.parser.BooleanNode;

import java.io.IOException;
import java.util.List;

public interface GatewayLogic {

    Owner createOwner(Owner owner) throws IOException;
    Boolean deleteOwnerByEmail(String email) throws IOException;
    Owner getOwnerByEmail(String email) throws IOException;
    Owner getOwnerById(String id) throws IOException;
    Boolean deleteOwnerByID(String id) throws IOException;
    Owner updateOwner(String email, Owner owner) throws IOException;

    Trucker createTrucker(Trucker trucker) throws IOException;
    Boolean deleteTruckerByEmail(String email);
    Boolean deleteTruckerByID(String id);
    Trucker getTruckerByEmail(String email);
    Trucker getTruckerByID(String id);
    Trucker updateTrucker(String email, Trucker trucker) throws IOException;

    String parkingAddress = "http://172.31.6.11:8083";
    Parking createParking(Parking parking) throws IOException;
    Parking getParkingById(String id);
    Boolean deleteParkingById(String id);
    List<Parking> getParkingByIdOwner(String id);
    List<Parking> getAllParking();
    Boolean updateParking(String id_owner, Parking parking) throws IOException;
    List<Parking> getParkingByCity(String city) throws IOException;

//    Boolean authenticateUser(String email, String pass);

    String bookingAddress = "http://172.31.6.11:8084";
    Booking createBooking(Booking b);
    Booking getBookingById(String id);
    List<Booking> getBookingByIdTrucker(String id_trucker);
    List<Booking> getBookingByIdParking(String id_parking);
    List<Booking> getAllBooking();
    Boolean deleteBookingById(String id);

    List<Booking> getBookingProva(String id);
}
