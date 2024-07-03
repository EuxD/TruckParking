package it.unisannio.ingsw24.booking.Controller;

import it.unisannio.ingsw24.Entities.Booking.Booking;
import it.unisannio.ingsw24.booking.persistence.BookingDAOMongo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.web.bind.annotation.RequestBody;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/booking")
public class BoookingRestController {

    private BookingDAOMongo bookingDAOMongo = BookingDAOMongo.getIstance();

    @POST
    @Path("/create")
    public Response createBooking(@RequestBody Booking bo) {
        Booking booking = bookingDAOMongo.createBooking(bo);
        if(booking == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.CREATED).build();
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
