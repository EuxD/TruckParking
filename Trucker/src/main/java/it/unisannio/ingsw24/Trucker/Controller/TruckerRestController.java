package it.unisannio.ingsw24.Trucker.Controller;

import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import it.unisannio.ingsw24.Trucker.Persistence.TruckerDAO;
import it.unisannio.ingsw24.Trucker.Persistence.TruckerDAOMongo;
import it.unisannio.ingsw24.Trucker.utils.EmailAlreadyExistsException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/trucker")
public class TruckerRestController {

    @Autowired
//    @Qualifier("truckerDAO") //indico quale BEAN andare ad iniettare (in questo caso posso anche evitare, dato che è uno)
    private TruckerDAO truckerDAOMongo;

    @POST
    @Path("/create")
    public Response createTrucker(@RequestBody Trucker t) {
        try {
            Trucker trucker = truckerDAOMongo.createTrucker(t);
            if (trucker == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Errore nella creazione del Trucker")
                        .type(MediaType.TEXT_PLAIN).build();
            }

            return Response.status(Response.Status.CREATED)
                    .entity(trucker)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (EmailAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN).build();
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    @GET
    @Path("ID/{id}")
    public Response getTruckerById(@PathParam("id") String id) {
        try{
            Trucker t = truckerDAOMongo.findTruckerById(id);
            return Response.ok()
                    .entity(t)
                    .type(MediaType.APPLICATION_JSON)
                    .build();


        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessun Trucker trovato con il seguente ID: " + id)
                    .type(MediaType.TEXT_PLAIN)
                    .build();

        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Errore: " + e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

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
                    .entity("Nessun Trucker con questa email: " + email)
                    .type(MediaType.TEXT_PLAIN)
                    .build();

        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Errore: " + e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    @DELETE
    @Path("/deleteID/{id}")
    public Response deleteTruckerByID(@PathParam("id") String id) {
        try {
            if (truckerDAOMongo.deleteTruckerByID(id)) {
                return Response.ok()
                        .entity("Trucker eliminato con successo")
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nessun Trucker trovato con questo ID: " + id)
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            }

        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Errore: " + e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();

        }
    }

    @PUT
    @Path("/update/{email}")
    public Response updateTrucker(@PathParam("email") String email, Trucker t) {
        try {
            Trucker updatedTrucker = truckerDAOMongo.updateTrucker(email, t);
            if (updatedTrucker == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Trucker non trovato o aggiornamento non riuscito")
                        .type(MediaType.TEXT_PLAIN).build();
            }

            return Response.ok()
                    .entity("Trucker modificato con successo")
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
                    .entity("Errore interno del server durante l'aggiornamento del trucker.")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

}


