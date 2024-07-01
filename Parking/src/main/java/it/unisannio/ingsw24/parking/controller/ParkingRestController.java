package it.unisannio.ingsw24.parking.controller;

import it.unisannio.ingsw24.Entities.Parking.Parking;
import it.unisannio.ingsw24.parking.persistence.ParkingDAOMongo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.web.bind.annotation.RequestBody;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/parking")
public class ParkingRestController {

   private ParkingDAOMongo parkingDAOMongo = ParkingDAOMongo.getIstance();

    @POST
    @Path("/create")
    public Response createParking(@RequestBody Parking p) {
        System.out.println(p.getnPlace());
        Parking parking = parkingDAOMongo.createParking(p);
        if(parking == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Errore nella creazione del parcheggio")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(parking)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/id/{id}")
    public Response findParkingById(@PathParam("id") String id) {
        Parking p = parkingDAOMongo.findParkingById(id);
        if(p == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessun Parcheggio con quel ID")
                    .build();
        }
        return Response.ok().entity(p).build();
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deleteParkingById(@PathParam("id") String id) {
        if(parkingDAOMongo.deleteParkingById(id)) {
            return Response.ok()
            .entity("Parcheggio eliminato").type(MediaType.TEXT_PLAIN).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Parcheggio non trovato")
                .type(MediaType.TEXT_PLAIN)
                .build();

    }

}
