package it.unisannio.ingsw24.Trucker.Controller;

import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import it.unisannio.ingsw24.Trucker.Persistence.TruckerDAOMongo;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@RequestMapping("/trucker")
public class TruckerRestController {

    private TruckerDAOMongo truckerDAOMongo = TruckerDAOMongo.getIstance();

    @GetMapping("/{email}")
    public ResponseEntity<?> getTruckerByEmail(@PathVariable("email") String email) {
        try {
            Trucker t = truckerDAOMongo.findTruckerByEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(t);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found, try a different email");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Multiple users found with the same email");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTrucker(@RequestBody Trucker t) {
        Trucker trucker = truckerDAOMongo.createTrucker(t);
        if(trucker == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Errore nella creazione del Trucker");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(trucker);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getTruckerById(@PathVariable("id") String id) {
        Trucker t = truckerDAOMongo.findTruckerById(id);
        if (t == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trucker not found, try a different id");
        }
        return ResponseEntity.status(HttpStatus.OK).body(t);
    }



}
