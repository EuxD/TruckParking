package it.unisannio.ingsw24.Trucker;

import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import it.unisannio.ingsw24.Trucker.Controller.TruckerRestController;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.assertEquals;

class TruckerRestControllerTest {

    static TruckerRestController truckerRestController = new TruckerRestController();

    static Trucker trucker;

    boolean isTruckerDeleted = false;

    @BeforeEach //prima di ogni caso di test mi crea il mio Trucker
    public void setup() {
        trucker = new Trucker("Massimo", "Pitagora", LocalDate.of(1990, 1, 1), "pitagora@gmail.com", "male", "ROLE_TRUCKER", "password", null);
        isTruckerDeleted = false;
    }

    @AfterEach
    public void cleanup() {
        // Viene chiamato dopo ogni caso di test
        if (!isTruckerDeleted && trucker != null && trucker.getId_trucker() != null) {
            truckerRestController.deleteTruckerByID(trucker.getId_trucker());
        }
    }

    @Test
    public void testCreateTruckerSuccess(){
        // Testo che la creazione del Trucker mi restituisca un 201
        Response response = truckerRestController.createTrucker(trucker);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(trucker, response.getEntity());

    }

    @Test
    public void testCreateTruckerWithExistingEmail(){

        Response response = truckerRestController.createTrucker(trucker);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        //Vado a creare un nuovo Trucker con la stessa mail di quello nel setup
        Trucker t1 = new Trucker("Massimo", "Parolo", LocalDate.of(1980, 12, 3), "pitagora@gmail.com", "male", "ROLE_TRUCKER", "1234", null);

        Response responseConfilct = truckerRestController.createTrucker(t1);
        assertEquals(Response.Status.CONFLICT.getStatusCode(), responseConfilct.getStatus());
        assertEquals("Esiste già un account legato a questa mail: " + t1.getEmail(),responseConfilct.getEntity());

        truckerRestController.deleteTruckerByID(t1.getId_trucker()); //cancello l'Trucker creato per il test
    }

    @Test
    public void testCreateTruckerWithInvalidBDate(){
        //Vado a creare un nuovo Trucker con la stessa mail di quello nel setup
        Trucker t1 = new Trucker("Massimo", "Parolo", LocalDate.of(3000, 12, 3), "parolo@example.com", "male", "ROLE_TRUCKER", "1234",null);

        Response response = truckerRestController.createTrucker(t1);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Data di nascita non valida",response.getEntity());

    }

    @Test
    public void testGetTruckerByEmailSuccess(){
        // Trucker già creato il @BefarEach, ma lo devo aggiungere prima per verificare che ci sia
        truckerRestController.createTrucker(trucker);
        Response response = truckerRestController.getTruckerByEmail(trucker.getEmail());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(trucker, response.getEntity());

    }

    @Test
    public void testGetTruckerByEmailFail(){
        // Test nel caso la lista è vuota
        Response response = truckerRestController.getTruckerByEmail("");
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun Trucker con questa email: ", response.getEntity());

    }

    @Test
    public void testGetTruckerByIDSuccess(){
        // Trucker già creato il @BefarEach, ma lo devo aggiungere prima per verificare che ci sia
        truckerRestController.createTrucker(trucker);
        Response response = truckerRestController.getTruckerById(trucker.getId_trucker());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(trucker, response.getEntity());

    }

    @Test
    public void testGetTruckerByIDFail(){
        // Test nel caso la lista è vuota
        Response response = truckerRestController.getTruckerById("");
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun Trucker trovato con il seguente ID: ", response.getEntity());

    }

    @Test
    public void testDeleteTruckerByIDSuccess(){
        // creo prima Trucker dato che ID assegno automatico al momento della creazione
        truckerRestController.createTrucker(trucker);

        Response response = truckerRestController.deleteTruckerByID(trucker.getId_trucker());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Trucker eliminato con successo", response.getEntity());
    }

    @Test
    public void testDeleteTruckerByIDFail(){
        //caso in cui ID non presente in DB
        Response response = truckerRestController.deleteTruckerByID("");
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun Trucker trovato con questo ID: ", response.getEntity());
    }

//    @Test
//    public void testDeleteTruckerByIDFailForBooking(){
//
//        Booking booking = new Booking("B01",trucker.getId_trucker(),"P01",LocalDate.now(),LocalTime.now(), LocalTime.now(), 10.0);
//
//        // Create a Trucker with an active booking
//        trucker.setBookings(List.of(booking.getId_booking()));
//
//        // Save the Trucker to the database
//        truckerRestController.updateTrucker(trucker.getEmail(), trucker);
//
//        // Try to delete the Trucker
//        Response response = truckerRestController.deleteTruckerByID(trucker.getId_trucker());
//
//        // Assert that the deletion fails due to active bookings
//        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
//        assertEquals("Impossibile eliminare il Trucker perchè ci sono delle prenotazioni in corso", response.getEntity());
//    }

    @Test
    public void testUpdateTruckerSuccess() {

        // Crea il proprietario nel database
        truckerRestController.createTrucker(trucker);

        // Aggiorna il proprietario
        Trucker updatedTrucker = new Trucker("nomeAggiornato", "cognomeAggiornato", LocalDate.of(1990, 1, 1), "email@example.com", "male", "ROLE_TRUCKER", "newPassword", null);

        // metto trucker.getEmail(), perchè anche se passo una nuova mail, non viene aggiornata - vedere impl metodo update
        Response responseUpdate = truckerRestController.updateTrucker(trucker.getEmail(), updatedTrucker);
        assertEquals(Response.Status.OK.getStatusCode(), responseUpdate.getStatus());
        assertEquals("Trucker modificato con successo", responseUpdate.getEntity());

    }

    @Test
    public void testUpdateTruckerNotFound() {
        // Aggiorna un proprietario che non esiste
        Trucker updatedTrucker = new Trucker("nomeAggiornato", "cognomeAggiornato", LocalDate.of(1990, 1, 1), "email@example.com", "male", "ROLE_TRUCKER", "newPassword", null);

        Response responseUpdate = truckerRestController.updateTrucker(trucker.getEmail(), updatedTrucker);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), responseUpdate.getStatus());
        assertEquals("Trucker non trovato o aggiornamento non riuscito", responseUpdate.getEntity());
    }


}