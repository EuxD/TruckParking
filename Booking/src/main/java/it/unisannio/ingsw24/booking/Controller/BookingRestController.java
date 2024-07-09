package it.unisannio.ingsw24.booking.Controller;

import it.unisannio.ingsw24.Entities.Booking.Booking;
import it.unisannio.ingsw24.Entities.Parking.Parking;
import it.unisannio.ingsw24.booking.persistence.BookingDAOMongo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/booking")
public class BookingRestController {

    private BookingDAOMongo bookingDAOMongo = BookingDAOMongo.getIstance();

    @POST
    @Path("/create")
    public Response createBooking(@RequestBody Booking bo) throws IOException {
        try {
            Booking booking = bookingDAOMongo.createBooking(bo);
            if (booking == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Impossibile creare la prenotazione")
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            }
            return Response.status(Response.Status.CREATED)
                    .entity(booking)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (IllegalStateException e){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    @GET
    @Path("ID/{id}")
    public Response getBookingById(@PathParam("id") String id) {
        try{
            Booking b = bookingDAOMongo.findBookingById(id);
            return Response.ok()
                    .entity(b)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (NoSuchElementException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessuna prenotazione con questo ID: " + id)
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    @GET
    @Path("truckerID/{id}")
    public Response getBookingByIdTrucker(@PathParam("id") String id) {
        try {
            List<Booking> bookings = bookingDAOMongo.getBookingByIdTrucker(id);
            return Response.ok()
                    .entity(bookings)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessuna prenotazione legata a questo trucker")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }


    }

    @GET
    @Path("/parkingID/{id}")
    public Response getBookingByIdParking(@PathParam("id") String id) {
        try {
            List<Booking> bookings = bookingDAOMongo.getBookingByIdParking(id);
            return Response.ok()
                    .entity(bookings)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessuna prenotazione legata a questo parcheggio")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

    }

    @GET
    public Response getAllBookings() {
        try {
            List<Booking> bookings = bookingDAOMongo.getAllBooking();
            return Response.ok()
                    .entity(bookings)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Non ci sonon prenotazioni")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deleteBookingById(@PathParam("id") String id) {
        try{
            if (bookingDAOMongo.deleteBookingById(id)) {
                return Response.ok()
                        .entity("Booking eliminato con successo")
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nessun Booking trovato con questo ID: " + id)
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            }
        }catch(IllegalStateException | IOException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Errore: " + e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();

        }
    }

}
