package it.unisannio.ingsw24.Trucker.Controller;

import it.unisannio.ingsw24.Entities.Trucker.DTO.TruckerLogin;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import it.unisannio.ingsw24.Trucker.Persistence.TruckerDAOMongo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/trucker")
public class TruckerRestController {

    private TruckerDAOMongo truckerDAOMongo = TruckerDAOMongo.getIstance();

    @GET
    @Path("/{email}")
    public Response getTruckerByEmail(@PathParam("email") String email) {
        try {
            Trucker t = truckerDAOMongo.findTruckerByEmail(email);
            return Response.ok()
                    .entity(t)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessun Trucker trovato con quella mail")
                    .type(MediaType.TEXT_PLAIN)
                    .build();

        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Più utenti con la stessa mail")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    @POST
    @Path("/create")
    public Response createTrucker(@RequestBody Trucker t) {
        Trucker trucker = truckerDAOMongo.createTrucker(t);
        if(trucker == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Errore nella creazione del Trucker")
                    .type(MediaType.TEXT_PLAIN).build();
        }
        return Response.status(Response.Status.CREATED)
                .entity(trucker).type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("id/{id}")
    public Response getTruckerById(@PathParam("id") String id) {
        Trucker t = truckerDAOMongo.findTruckerById(id);
        if (t == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Nessun Trucker con quel ID").build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/delete/{email}")
    public Response deleteTruckerByEmail(@PathParam("email") String email) {
        Trucker deletedTrucker = truckerDAOMongo.deleteTruckerByEmail(email);
        if (deletedTrucker == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Trucker not found")
                    .type(MediaType.TEXT_PLAIN).build();
        }
        return Response.status(Response.Status.OK)
                .entity(deletedTrucker)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

//    @DELETE
//    @Path("/deleteTrucker") //de gestire meglio le eccezioni come fatto per findByMail
//    public Response deleteTruckerByEmail(@QueryParam("email") String email){
//        boolean flag = truckerDAOMongo.deleteTruckerByEmail(email);
//        if(flag){
//            return Response.ok().entity("Trucker eliminato con successo").type(MediaType.TEXT_PLAIN).build();
//        } else {
//            return Response.status(Response.Status.NOT_FOUND).entity("Trucker non trovato")
//                    .entity(MediaType.TEXT_PLAIN).build();
//        }
//    }



}
