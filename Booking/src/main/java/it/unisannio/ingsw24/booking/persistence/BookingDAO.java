package it.unisannio.ingsw24.booking.persistence;

import it.unisannio.ingsw24.Entities.Booking.Booking;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public interface BookingDAO {

    String DATABASE_NAME = "TruckParking";
    String COLLECTION_NAME = "Booking";
    String ELEMENT_ID = "id_booking";
    String ELEMENT_ID_TRUCKER = "id_trucker";
    String ELEMENT_ID_PARKING = "id_parking";
    String ELEMENT_PDATE = "pDate";
    String ELEMENT_ORA_INIZIO = "ora_inizio";
    String ELEMENT_ORA_FINE = "ora_fine";
    String ELEMENT_TOTALE = "total";

    Booking createBooking(Booking booking) throws IOException;
    Booking findBookingById(String id);
    List<Booking> getBookingByIdParking(String id_parking);
    List<Booking> getBookingByIdTrucker(String id_trucker);
    List<Booking> getAllBooking();

    Boolean deleteBookingById(String id) throws IOException;



}
