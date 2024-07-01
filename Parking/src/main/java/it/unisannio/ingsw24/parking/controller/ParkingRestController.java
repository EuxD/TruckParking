package it.unisannio.ingsw24.parking.controller;

import it.unisannio.ingsw24.Entities.Parking.Parking;
import it.unisannio.ingsw24.parking.persistence.ParkingDAOMongo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.NoSuchElementException;

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
    public Response getParkingById(@PathParam("id") String id) {
        Parking p = parkingDAOMongo.findParkingById(id);
        if(p == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessun Parcheggio con quel ID")
                    .build();
        }
        return Response.ok().entity(p).build();
    }

    @GET
    @Path("/idOwner/{id_owner}")
    public Response getParkingByIdOwner(@PathParam("id_owner") String id){
        try {
            List<Parking> parking = parkingDAOMongo.findParkingByIdOwner(id);
            return Response.ok()
                    .entity(parking)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (NoSuchElementException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessun parcheggio riferito a questo id owner")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    @GET
    public Response getAllParkings(){
        try {
            List<Parking> parking = parkingDAOMongo.getAllParking();
            return Response.ok()
                    .entity(parking)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (NoSuchElementException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessun parcheggio")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
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

    @PUT
    @Path("/update")
    public Response updateParking(@RequestBody Parking p) {
        if(parkingDAOMongo.updateParking(p)){
            return Response.ok()
                    .entity("Parking modificato con successo")
                    .type(MediaType.TEXT_PLAIN)
                    .build();

        }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Parking not found")
                    .type(MediaType.TEXT_PLAIN)
                    .build();


    }

}
