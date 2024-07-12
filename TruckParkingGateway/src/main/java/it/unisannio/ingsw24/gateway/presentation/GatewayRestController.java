package it.unisannio.ingsw24.gateway.presentation;


import it.unisannio.ingsw24.Entities.Booking.Booking;
import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Parking.Parking;
import it.unisannio.ingsw24.Entities.Trucker.*;
import it.unisannio.ingsw24.gateway.logic.GatewayLogic;
import it.unisannio.ingsw24.gateway.logic.GatewayLogicImpl;

import it.unisannio.ingsw24.gateway.utils.BookingCreateException;
import it.unisannio.ingsw24.gateway.utils.BookingNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;


import java.io.IOException;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/rest")
public class GatewayRestController {

    GatewayLogic logic;

    public GatewayRestController() {
        logic = new GatewayLogicImpl();
    }

    //////////////////////////////// TRUCKER ////////////////////////////////

    @POST
    @Path("/createTrucker")
    public Response createTrucker(@RequestBody Trucker t) throws IOException {
        Trucker trucker = logic.createTrucker(t);
        if (trucker == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Errore nella creazione del Trucker")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }



        return Response.ok().entity("Registrazione avvenuta con successo").type(MediaType.TEXT_PLAIN)
                .build();
    }

    @GET
    @Path("/trucker/email/{email}")
    public Response getTruckerByEmail(@PathParam("email") String email) {
        Trucker trucker = logic.getTruckerByEmail(email);
        if (trucker == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Nessun trucker trovato")
                    .type(MediaType.TEXT_HTML)
                    .build();
        }

        return Response.ok().entity(trucker).type(MediaType.APPLICATION_JSON).build();
        // FUNZIONA
    }

    @GET
    @Path("/trucker/ID/{id}")
    public Response getTruckerByID(@PathParam("id") String id) {
        Trucker trucker = logic.getTruckerByID(id);
        if (trucker == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Nessun trucker trovato")
                    .type(MediaType.TEXT_HTML)
                    .build();
        }

        return Response.ok().entity(trucker).type(MediaType.APPLICATION_JSON).build();
        // FUNZIONA
    }

    @PUT
    @Path("/trucker/update/{email}")
    public Response updateTrucker(@PathParam("email") String email, @RequestBody Trucker trucker) throws IOException {
        Trucker t = logic.updateTrucker(email, trucker);
        if (t == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Errore nella modifica del Trucker")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/trucker/delete/email/{email}")
    public Response deleteTruckerByEmail(@PathParam("email") String email) {
        boolean flag = logic.deleteTruckerByEmail(email);
        if (flag) {
            return Response.ok().entity("Trucker eliminato con successo").type(MediaType.TEXT_PLAIN).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Trucker non trovato").build();
        }
        // FUNZIONA
    }

    @DELETE
    @Path("/trucker/delete/ID/{id}")
    public Response deleteTruckerByID(@PathParam("id") String id) {
        boolean flag = logic.deleteTruckerByID(id);
        if (flag) {
            return Response.ok().entity("Trucker eliminato con successo").type(MediaType.TEXT_PLAIN).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Trucker non trovato").build();
        }
        // FUNZIONA
    }

    //////////////////////////////////// OWNER ////////////////////////////////////

    @POST
    @Path("/createOwner")
    public Response createOwner(@RequestBody Owner ow) throws IOException {
        Owner owner = logic.createOwner(ow);
        if (owner == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Errore nella creazione dell'Owner")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return Response.ok().entity("Registrazione avvenuta con successo").type(MediaType.TEXT_PLAIN).build();

    } // FUNZIONA

    @GET
    @Path("/owner/email/{email}")
    public Response getOwnerByEmail(@PathParam("email") String email) throws IOException {
        Owner o = logic.getOwnerByEmail(email);
        if (o == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Owner non trovato")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        return Response.ok().entity(o).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/owner/ID/{id}")
    public Response getOwnerByID(@PathParam("id") String id) throws IOException {
        Owner o = logic.getOwnerById(id);
        if (o == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Owner non trovato")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        return Response.ok()
                .entity(o).type(MediaType.APPLICATION_JSON).build();
    } // FUNZIONA

    @PUT
    @Path("/owner/update/{email}")
    public Response updateOwner(@PathParam("email") String email, @RequestBody Owner ow) throws IOException {
        Owner o = logic.updateOwner(email, ow);
        if (o == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Errore nella modifica del Trucker")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/owner/delete/email/{email}")
    public Response deleteOwnerByEmail(@PathParam("email") String email) throws IOException {
        boolean flag = logic.deleteOwnerByEmail(email);
        if (flag) {
            return Response.ok().entity("Owner eliminato con successo").type(MediaType.TEXT_PLAIN).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Owner non trovato").build();
        }
    }

    @DELETE
    @Path("/owner/delete/ID/{id}")
    public Response deleteOwnerByID(@PathParam("id") String id) throws IOException {
        boolean flag = logic.deleteOwnerByID(id);
        if (flag) {
            return Response.ok().entity("Owner eliminato con successo").type(MediaType.TEXT_PLAIN).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Owner non trovato").build();
        }
    } // FUNZIONA

    //////////////////////////////////// PARKING ////////////////////////////////////

    @POST
    @Path("/parking/create")
    public Response createParking(@RequestBody Parking parking) throws IOException {
        Parking p = logic.createParking(parking);
        if (p == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Errore nella creazione del parcheggio")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity("Parcheggio registrato con successo")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    @GET
    @Path("/parking")
    public Response getAllParking() {
        List<Parking> parkings = logic.getAllParking();
        if (parkings == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Nessun parcheggio trovato")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return Response.ok()
                .entity(parkings)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/parking/ID/{id}")
    public Response getParkingByID(@PathParam("id") String id) {
        Parking parking = logic.getParkingById(id);
        if (parking == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Nessun parhceggio con quell'ID")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return Response.ok()
                .entity(parking)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/parking/ownerID/{id}")
    public Response getParkingByIDOwner(@PathParam("id") String id) {
        List<Parking> parkings = logic.getParkingByIdOwner(id);
        if (parkings == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Nessun parcheggio legato a quell'Owner")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return Response.ok()
                .entity(parkings)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @PUT
    @Path("/parking/update/{id_owner}")
    public Response updateParking(@PathParam("id_owner") String id, @RequestBody Parking parking) throws IOException {
        boolean flag = logic.updateParking(id, parking);
        if (flag) {
            return Response.ok().entity("Parcheggio aggiornato con successo")
                    .type(MediaType.TEXT_PLAIN).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Parcheggio non trovato")
                    .type(MediaType.TEXT_PLAIN).build();
        }
    }

    @DELETE
    @Path("/parking/delete/{id}")
    public Response deleteParking(@PathParam("id") String id) {
        boolean flag = logic.deleteParkingById(id);
        if (flag) {
            return Response.ok().entity("Parcheggio eliminato con successo")
                    .type(MediaType.TEXT_PLAIN).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Parcheggio non trovato").build();
        }
    }

    //////////////////////////////////// BOOKING ////////////////////////////////////

    @POST
    @Path("/booking/create")
    public Response createBooking(@RequestBody Booking booking){
        try{
            Booking b = logic.createBooking(booking);

//            //Invio dell'email
//            EmailService emailService = new EmailService();
//            String subject = "Prenotazione aeffettuata con successo";
//            String body = "";
//            emailService.sendEmail(logic.getTruckerByID(booking.getId_trucker()).getEmail(),body,subject);

            return Response.status(Response.Status.CREATED)
                    .entity(b)
                    .type(MediaType.APPLICATION_JSON)
                    .build();


        } catch (BookingCreateException e){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }


    }

    @GET
    @Path("/booking/ID/{id}")
    public Response getBookingById(@PathParam("id") String id_booking){
        try{
            Booking b = logic.getBookingById(id_booking);
            return Response.ok()
                    .entity(b)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (BookingNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    @GET
    @Path("/booking/truckerID/{id}")
    public Response getBookingByIdTrucker(@PathParam("id") String id_trucker) {
        try {
            List<Booking> bookings = logic.getBookingByIdTrucker(id_trucker);
            return Response.ok()
                    .entity(bookings)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (BookingNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    @GET
    @Path("/booking/parkingID/{id}")
    public Response getBookingByIdParking(@PathParam("id") String id_park){
        try{
            List<Booking> bookings = logic.getBookingByIdParking(id_park);
            return Response.ok()
                    .entity(bookings)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (BookingNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    @DELETE
    @Path("/booking/delete/{id}")
    public Response deleteBookingById(@PathParam("id") String id_booking){
        boolean flag = logic.deleteBookingById(id_booking);
        if (flag) {
            return Response.ok().entity("Prenotazione eliminata con successo")
                        .type(MediaType.TEXT_PLAIN).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Prenotazione non trovata").build();
        }
    }
}



