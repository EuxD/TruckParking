package it.unisannio.ingsw24.Owner.Controller;

import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import it.unisannio.ingsw24.Owner.Persistence.OwnerDAOMongo;
import it.unisannio.ingsw24.Owner.utils.EmailAlreadyExistsException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/owner")
public class OwnerRestController {

    private OwnerDAOMongo ownerDAOMongo = OwnerDAOMongo.getIstance();

    @POST
    @Path("/create")
    public Response createOwner(@RequestBody Owner ow) {
        try {
            Owner owner = ownerDAOMongo.createOwner(ow);
            if (owner == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Errore nella creazione del Trucker")
                        .type(MediaType.TEXT_PLAIN).build();
            }

            return Response.status(Response.Status.CREATED)
                    .entity(owner)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (EmailAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN).build();
        }
    }

    @GET
    @Path("/{email}")
    public Response getOwnerByEmail(@PathParam("email") String email) {
        try {
            Owner o = ownerDAOMongo.findOwnerByEmail(email);
            return Response.ok()
                    .entity(o)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessun Owner con questa email: " + email)
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
    @Path("ID/{id}")
    public Response getOwnerByID(@PathParam("id") String id) {
        try {
            Owner o = ownerDAOMongo.findOwnerById(id);
            return Response.ok()
                    .entity(o)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessun Owner con questo id: " + id)
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
    @Path("/delete/{email}")
    public Response deleteTruckerByEmail(@PathParam("email") String email) {
        try {
        if (ownerDAOMongo.deleteOwnerByEmail(email)) {
            return Response.ok()
                    .entity("Owner eliminato con successo")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessun Owner trovato con questa email: " + email)
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

    @DELETE
    @Path("/deleteID/{id}")
    public Response deleteOwnerByID(@PathParam("id") String id) {
        try {
            if (ownerDAOMongo.deleteOwnerByID(id)) {
                return Response.ok()
                        .entity("Owner eliminato con successo")
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nessun Owner trovato con questo ID: " + id)
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateOwner(@PathParam("email") String email, Owner o) {
        try {
            Owner updatedOwner = ownerDAOMongo.updateOwner(email, o);
            if (updatedOwner == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Owner non trovato o aggiornamento non riuscito")
                        .type(MediaType.TEXT_PLAIN).build();
            }

            return Response.ok()
                    .entity("Owner modificato con successo")
                    .type(MediaType.TEXT_HTML)
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Errore: " + e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();

        } catch (Exception e) {
            e.printStackTrace(); // Log dell'errore
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Errore interno del server durante l'aggiornamento dell'owner.")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }


}
