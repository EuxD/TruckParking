package it.unisannio.ingsw24.gateway.presentation;

import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Trucker.*;
import it.unisannio.ingsw24.Entities.Trucker.DTO.TruckerLogin;
import it.unisannio.ingsw24.gateway.logic.GatewayLogic;
import it.unisannio.ingsw24.gateway.logic.GatewayLogicImpl;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/rest")
public class GatewayRestController {

    GatewayLogic logic;

    public GatewayRestController() {
        logic = new GatewayLogicImpl();
    }

    @POST
    @Path("/createTrucker")
    public Response createTrucker(@RequestBody Trucker t) throws IOException {
        Trucker trucker = logic.createTrucker(t);
        if(trucker == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Errore nella creazione del Trucker")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        return Response.ok().build();
        //FUNZIONA
    }

    @GET
    @Path("/trucker/email/{email}")
    public Response getTruckerByEmail(@PathParam("email") String email){
        Trucker trucker = logic.getTruckerByEmail(email);
        if(trucker == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Nessun trucker trovato")
                    .type(MediaType.TEXT_HTML)
                    .build();
        }

        return Response.ok().entity(trucker).type(MediaType.APPLICATION_JSON).build();
        // mi recupera anche un utente con ruolo owner, da controllare
        //FUNZIONA
    }

    @GET
    @Path("/trucker/ID/{id}")
    public Response getTruckerByID(@PathParam("id") String id){
        Trucker trucker = logic.getTruckerByID(id);
        if(trucker == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Nessun trucker trovato")
                    .type(MediaType.TEXT_HTML)
                    .build();
        }

        return Response.ok().entity(trucker).type(MediaType.APPLICATION_JSON).build();
        //FUNZIONA
    }

    @PUT
    @Path("/trucker/update")
    public Response updateTrucker(@RequestBody Trucker trucker) throws IOException {
        Trucker t = logic.updateTrucker(trucker);
        if(t == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Errore nella modifica del Trucker")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        return Response.ok().build();
    }


    @DELETE
    @Path("/trucker/delete/email/{email}")
    public Response deleteTruckerByEmail(@PathParam("email") String email){
        boolean flag = logic.deleteTruckerByEmail(email);
        if(flag){
            return Response.ok().entity("Trucker eliminato con successo").type(MediaType.TEXT_PLAIN).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Trucker non trovato").build();
        }
        //FUNZIONA
    }

    @DELETE
    @Path("/trucker/delete/ID/{id}")
    public Response deleteTruckerByID(@PathParam("id") String id){
        boolean flag = logic.deleteTruckerByID(id);
        if(flag){
            return Response.ok().entity("Trucker eliminato con successo").type(MediaType.TEXT_PLAIN).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Trucker non trovato").build();
        }
        //FUNZIONA
    }

    @POST
    @Path("/createOwner")
    public Response createOwner(@RequestBody Owner ow) throws IOException {
        Owner owner = logic.createOwner(ow);
        if(owner == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Errore nella creazione dell'Owner")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        return Response.ok()
//                .entity(owner.toString())
//                .type(MediaType.TEXT_HTML)
                .build();
    }

    @GET
    @Path("/owner/email/{email}")
    public Response getOwnerByEmail(@PathParam("email") String email) throws IOException {
        Owner o = logic.getOwnerByEmail(email);
        if(o == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Owner non trovato")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        return Response.ok().build();
        //FUNZIONA
    }




}
