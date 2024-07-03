package it.unisannio.ingsw24.booking.Controller;

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
