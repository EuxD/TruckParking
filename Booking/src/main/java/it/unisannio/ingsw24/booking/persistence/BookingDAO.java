package it.unisannio.ingsw24.booking.persistence;

import it.unisannio.ingsw24.Entities.Booking.Booking;

public interface BookingDAO {

    String DATABASE_NAME = "TruckParking";
    String COLLECTION_NAME = "Booking";
    String ELEMENT_ID = "id_booking";
    String ELEMENT_ID_TRUCKER = "id_trucker";
    String PARKING = "id_parking";
    String ELEMENT_PDATE = "pDate";

    Booking createBooking(Booking booking);
    Booking findBookingById(String id);
    Booking deleteBookingById(String id);
}
