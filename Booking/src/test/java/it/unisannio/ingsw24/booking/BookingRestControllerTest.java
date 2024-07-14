package it.unisannio.ingsw24.booking;

import it.unisannio.ingsw24.Entities.Booking.Booking;
import it.unisannio.ingsw24.booking.Controller.BookingRestController;
import it.unisannio.ingsw24.booking.persistence.BookingDAOMongo;
import it.unisannio.ingsw24.booking.utils.BookingNotExpiredException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BookingRestControllerTest {

    @InjectMocks
    private BookingRestController bookingRestController;

    @Mock
    private BookingDAOMongo bookingDAOMongo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBooking_Success() throws IOException {
        Booking booking = new Booking();
        when(bookingDAOMongo.createBooking(any(Booking.class))).thenReturn(booking);

        Response response = bookingRestController.createBooking(booking);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(booking, response.getEntity());
    }

    @Test
    public void testCreateBooking_Failure() throws IOException {
        Booking booking = new Booking();
        when(bookingDAOMongo.createBooking(any(Booking.class))).thenReturn(null);

        Response response = bookingRestController.createBooking(booking);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Impossibile creare la prenotazione", response.getEntity());
    }

    @Test
    public void testGetBookingById_Success() {
        Booking booking = new Booking();
        when(bookingDAOMongo.findBookingById("123")).thenReturn(booking);

        Response response = bookingRestController.getBookingById("123");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(booking, response.getEntity());
    }

    @Test
    public void testGetBookingById_NotFound() {
        when(bookingDAOMongo.findBookingById("123")).thenThrow(new NoSuchElementException());

        Response response = bookingRestController.getBookingById("123");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessuna prenotazione con questo ID: 123", response.getEntity());
    }

    @Test
    public void testGetBookingByIdTrucker_Success() {
        Booking booking = new Booking();
        List<Booking> bookings = Collections.singletonList(booking);
        when(bookingDAOMongo.getBookingByIdTrucker("truckerId")).thenReturn(bookings);

        Response response = bookingRestController.getBookingByIdTrucker("truckerId");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(bookings, response.getEntity());
    }

    @Test
    public void testGetBookingByIdTrucker_NotFound() {
        when(bookingDAOMongo.getBookingByIdTrucker("truckerId")).thenThrow(new NoSuchElementException());

        Response response = bookingRestController.getBookingByIdTrucker("truckerId");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessuna prenotazione legata a questo trucker", response.getEntity());
    }

    @Test
    public void testGetBookingByIdParking_Success() {
        Booking booking = new Booking();
        List<Booking> bookings = Collections.singletonList(booking);
        when(bookingDAOMongo.getBookingByIdParking("parkingId")).thenReturn(bookings);

        Response response = bookingRestController.getBookingByIdParking("parkingId");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(bookings, response.getEntity());
    }

    @Test
    public void testGetBookingByIdParking_NotFound() {
        when(bookingDAOMongo.getBookingByIdParking("parkingId")).thenThrow(new NoSuchElementException());

        Response response = bookingRestController.getBookingByIdParking("parkingId");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessuna prenotazione legata a questo parcheggio", response.getEntity());
    }

    @Test
    public void testGetAllBookings_Success() {
        Booking booking = new Booking();
        List<Booking> bookings = Collections.singletonList(booking);
        when(bookingDAOMongo.getAllBooking()).thenReturn(bookings);

        Response response = bookingRestController.getAllBookings();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(bookings, response.getEntity());
    }

    @Test
    public void testGetAllBookings_NotFound() {
        when(bookingDAOMongo.getAllBooking()).thenThrow(new NoSuchElementException());

        Response response = bookingRestController.getAllBookings();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Non ci sonon prenotazioni", response.getEntity());
    }

    @Test
    public void testAreAllBookingsTruckerExpired_BadRequest() throws BookingNotExpiredException {
        doThrow(new BookingNotExpiredException("Error message")).when(bookingDAOMongo).areAllBookingsExpired("truckerId");

        Response response = bookingRestController.areAllBookingsTruckerExpired("truckerId");

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Error message", response.getEntity());
    }

    @Test
    public void testDeleteBookingById_Success() throws IOException {
        when(bookingDAOMongo.deleteBookingById("123")).thenReturn(true);

        Response response = bookingRestController.deleteBookingById("123");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Booking eliminato con successo", response.getEntity());
    }

    @Test
    public void testDeleteBookingById_NotFound() throws IOException {
        when(bookingDAOMongo.deleteBookingById("123")).thenReturn(false);

        Response response = bookingRestController.deleteBookingById("123");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun Booking trovato con questo ID: 123", response.getEntity());
    }

    @Test
    public void testDeleteBookingById_Conflict() throws IOException {
        when(bookingDAOMongo.deleteBookingById("123")).thenThrow(new IOException("Error"));

        Response response = bookingRestController.deleteBookingById("123");

        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
        assertEquals("Errore: Error", response.getEntity());
    }
}
