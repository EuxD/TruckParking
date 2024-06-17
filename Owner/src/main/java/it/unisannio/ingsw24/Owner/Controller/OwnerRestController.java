package it.unisannio.ingsw24.Owner.Controller;

import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import it.unisannio.ingsw24.Owner.Persistence.OwnerDAOMongo;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@RequestMapping("/owner")
public class OwnerRestController {

    private OwnerDAOMongo ownerDAOMongo = OwnerDAOMongo.getIstance();

    @PostMapping("/create")
    public ResponseEntity<?> createTrucker(@RequestBody Owner ow) {
        Owner owner = ownerDAOMongo.createOwner(ow);
        if(owner == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Errore nella creazione del Trucker");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(owner);
    }
}
