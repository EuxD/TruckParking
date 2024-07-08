package it.unisannio.ingsw24.parking.controller;

import it.unisannio.ingsw24.Entities.Parking.Parking;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import it.unisannio.ingsw24.parking.persistence.ParkingDAO;
import it.unisannio.ingsw24.parking.persistence.ParkingDAOMongo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
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
        try{
            Parking p = parkingDAOMongo.findParkingById(id);
            return Response.ok()
                    .entity(p)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (NoSuchElementException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessun Parking con quell'ID: " + id)
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
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
        try {
            if (parkingDAOMongo.deleteParkingById(id)) {
                return Response.ok()
                        .entity("Parcheggio eliminato").type(MediaType.TEXT_PLAIN).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Parcheggio non trovato")
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            }
        } catch (IllegalStateException e){
            return Response.status(Response.Status.CONFLICT)
                    .entity("Errore nel database: "+ e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @PUT
    @Path("/update/{id_parking}")
    public Response updateParking(@PathParam("id_parking") String id, @RequestBody Parking p) {
        try {

            if (!parkingDAOMongo.updateParking(id, p)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Parking non trovato o aggiornamento non riuscito")
                        .type(MediaType.TEXT_PLAIN).build();
            }

            return Response.ok()
                    .entity("Parking modificato con successo")
                    .type(MediaType.TEXT_HTML)
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Errore: " + e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Errore interno del server durante l'aggiornamento del parking.")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

}
