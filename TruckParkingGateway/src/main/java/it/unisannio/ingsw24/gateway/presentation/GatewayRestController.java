package it.unisannio.ingsw24.gateway.presentation;

import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
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
    }
}
