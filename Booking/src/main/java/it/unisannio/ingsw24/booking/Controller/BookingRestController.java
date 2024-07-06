package it.unisannio.ingsw24.booking.Controller;

import it.unisannio.ingsw24.Entities.Booking.Booking;
import it.unisannio.ingsw24.booking.persistence.BookingDAOMongo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

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
                    .entity("Prenotazione effettuata con successo")
                    .type(MediaType.TEXT_PLAIN)
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
        Booking o = bookingDAOMongo.findBookingById(id);
        if (o == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessun Owner con quel ID")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        return Response.ok().entity(o).build();
    }

}
