package it.unisannio.ingsw24.Owner.Controller;

import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import it.unisannio.ingsw24.Owner.Persistence.OwnerDAOMongo;
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
        Owner owner = ownerDAOMongo.createOwner(ow);
        if(owner == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("ID/{id}")
    public Response getOwnerById(@PathParam("id") String id) {
        Owner o = ownerDAOMongo.findOwnerById(id);
        if (o == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nessun Owner con quel ID")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        return Response.ok().entity(o).build();
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
                    .entity("Nessun Owner con quel email")
                    .type(MediaType.TEXT_PLAIN)
                    .build();

        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Più utenti con la stessa mail")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

    }

    @DELETE
    @Path("/delete/{email}")
    public Response deleteOwnerByEmail(@PathParam("email") String email) {
        Owner deletedOwner = ownerDAOMongo.deleteOwnerByEmail(email);
        if (deletedOwner == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Owner not found")
                    .type(MediaType.TEXT_PLAIN).build();
        }
        return Response.status(Response.Status.OK)
                .entity(deletedOwner)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @DELETE
    @Path("/deleteID/{id}")
    public Response deleteOwnerByID(@PathParam("id") String id) {
        Owner deletedOwner = ownerDAOMongo.deleteOwnerByID(id);
        if (deletedOwner == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Owner not found")
                    .type(MediaType.TEXT_PLAIN).build();
        }
        return Response.ok()
                .entity(deletedOwner)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @PUT
    @Path("/update")
    public Response updateOwner(@RequestBody Owner o) {
        Owner updatedOwner = ownerDAOMongo.updateOwner(o);
        if (updatedOwner == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Owner not found")
                    .type(MediaType.APPLICATION_JSON).build();
        }

        return Response.ok()
                .entity("Owner modificato con successo")
                .type(MediaType.TEXT_HTML)
                .build();
    }


}
