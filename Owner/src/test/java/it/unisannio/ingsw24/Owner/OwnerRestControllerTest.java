package it.unisannio.ingsw24.Owner;

import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Owner.Controller.OwnerRestController;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OwnerRestControllerTest {

    static OwnerRestController ownerRestController = new OwnerRestController();

    static Owner owner;

    boolean isOwnerDeleted = false;

    @BeforeEach //prima di ogni caso di test mi crea il mio Owner
    public void setup() {
        owner = new Owner("Massimo", "Pitagora", LocalDate.of(1990, 1, 1), "pitagora@gmail.com", "male", "ROLE_OWNER", "password");
        isOwnerDeleted = false;
    }

    @AfterEach
    public void cleanup() {
        // Viene chiamato dopo ogni caso di test
        if (!isOwnerDeleted && owner != null && owner.getId_owner() != null) {
            ownerRestController.deleteOwnerByID(owner.getId_owner());
        }
    }

    @Test
    public void testCreateOwnerSuccess(){
        // Testo che la creazione dell'Owner mi restituisca un 201
        Response response = ownerRestController.createOwner(owner);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(owner, response.getEntity());

    }

    @Test
    public void testCreateOwnerWithExistingEmail(){

        Response response = ownerRestController.createOwner(owner);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        //Vado a creare un nuovo Owner con la stessa mail di quello nel setup
        Owner owner1 = new Owner("Massimo", "Parolo", LocalDate.of(1980, 12, 3), "pitagora@gmail.com", "male", "ROLE_OWNER", "1234");

        Response responseConfilct = ownerRestController.createOwner(owner1);
        assertEquals(Response.Status.CONFLICT.getStatusCode(), responseConfilct.getStatus());
        assertEquals("Esiste già un account legato a questa mail: " + owner1.getEmail(),responseConfilct.getEntity());

        ownerRestController.deleteOwnerByID(owner1.getId_owner()); //cancello l'owner creato per il test
    }

    @Test
    public void testCreateOwnerWithInvalidBDate(){
        //Vado a creare un nuovo Owner con la stessa mail di quello nel setup
        Owner owner1 = new Owner("Massimo", "Parolo", LocalDate.of(3000, 12, 3), "parolo@example.com", "male", "ROLE_OWNER", "1234");

        Response response = ownerRestController.createOwner(owner1);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Data di nascita non valida",response.getEntity());

    }

    @Test
    public void testGetOwnerByEmailSuccess(){
        // Owner già creato il @BefarEach, ma lo devo aggiungere prima per verificare che ci sia
        ownerRestController.createOwner(owner);
        Response response = ownerRestController.getOwnerByEmail(owner.getEmail());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    }

    @Test
    public void testGetOwnerByEmailFail(){
        // Test nel caso la lista è vuota
        Response response = ownerRestController.getOwnerByEmail("");
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun Owner con questa email: ", response.getEntity());

    }

    @Test
    public void testGetOwnerByIDSuccess(){
        // Owner già creato il @BefarEach, ma lo devo aggiungere prima per verificare che ci sia
        ownerRestController.createOwner(owner);
        Response response = ownerRestController.getOwnerByID(owner.getId_owner());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    }

    @Test
    public void testGetOwnerByIDFail(){
        // Test nel caso la lista è vuota
        Response response = ownerRestController.getOwnerByID("");
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun Owner con questo id: ", response.getEntity());

    }

    @Test
    public void testDeleteOwnerByIDSuccess(){
        // creo prima owner dato che ID assegno automatico al momento della creazione
        ownerRestController.createOwner(owner);

        Response response = ownerRestController.deleteOwnerByID(owner.getId_owner());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Owner eliminato con successo", response.getEntity());
    }

    @Test
    public void testDeleteOwnerByIDFail(){
        //caso in cui ID non presente in DB
        Response response = ownerRestController.deleteOwnerByID("");
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun Owner trovato con questo ID: ", response.getEntity());
    }

    @Test
    public void testDeleteOwnerByEmailSuccess(){
        // aggiungo alla collezione prima di eliminare
        ownerRestController.createOwner(owner);

        Response response = ownerRestController.deleteOwnerByEmail(owner.getEmail());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Owner eliminato con successo", response.getEntity());
    }

    @Test
    public void testDeleteOwnerByEmailFail(){
        //caso in cui ID non presente in DB
        Response response = ownerRestController.deleteOwnerByEmail("");
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun Owner trovato con questa email: ", response.getEntity());
    }

    @Test
    public void testUpdateOwnerSuccess() {

        // Crea il proprietario nel database
        ownerRestController.createOwner(owner);

        // Aggiorna il proprietario
        Owner updatedOwner = new Owner("nomeAggiornato", "cognomeAggiornato", LocalDate.of(1990, 1, 1), "email@example.com", "male", "ROLE_OWNER", "newPassword");

        // metto owner.getEmail(), perchè anche se passo una nuova mail, non viene aggiornata - vedere impl metodo update
        Response responseUpdate = ownerRestController.updateOwner(owner.getEmail(), updatedOwner);
        assertEquals(Response.Status.OK.getStatusCode(), responseUpdate.getStatus());
        assertEquals("Owner modificato con successo", responseUpdate.getEntity());

    }

    @Test
    public void testUpdateOwnerNotFound() {
        // Aggiorna un proprietario che non esiste
        Owner updatedOwner = new Owner("nomeAggiornato", "cognomeAggiornato", LocalDate.of(1990, 1, 1), "nonExistingEmail@example.com", "male", "ROLE_OWNER", "newPassword");

        Response responseUpdate = ownerRestController.updateOwner(updatedOwner.getEmail(), updatedOwner);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), responseUpdate.getStatus());
        assertEquals("Owner non trovato o aggiornamento non riuscito", responseUpdate.getEntity());
    }


    }