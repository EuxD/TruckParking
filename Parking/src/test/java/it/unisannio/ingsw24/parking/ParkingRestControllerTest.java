package it.unisannio.ingsw24.parking;

import it.unisannio.ingsw24.Entities.Parking.Parking;
import it.unisannio.ingsw24.parking.controller.ParkingRestController;
import it.unisannio.ingsw24.parking.persistence.ParkingDAOMongo;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ParkingRestControllerTest {

    @Mock
    private ParkingDAOMongo parkingDAOMongo;

    @InjectMocks
    private ParkingRestController parkingRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateParking_Success() {
        Parking parking = new Parking();
        when(parkingDAOMongo.createParking(any(Parking.class))).thenReturn(parking);

        Response response = parkingRestController.createParking(parking);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(parking, response.getEntity());
    }

    @Test
    public void testCreateParking_Failure() {
        Parking parking = new Parking();
        when(parkingDAOMongo.createParking(any(Parking.class))).thenReturn(null);

        Response response = parkingRestController.createParking(parking);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Errore nella creazione del parcheggio", response.getEntity());
    }

    @Test
    public void testGetParkingById_Success() {
        Parking parking = new Parking();
        when(parkingDAOMongo.findParkingById(anyString())).thenReturn(parking);

        Response response = parkingRestController.getParkingById("123");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(parking, response.getEntity());
    }

    @Test
    public void testGetParkingById_NotFound() {
        when(parkingDAOMongo.findParkingById(anyString())).thenThrow(new NoSuchElementException());

        Response response = parkingRestController.getParkingById("123");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun Parking con quell'ID: 123", response.getEntity());
    }

    @Test
    public void testGetParkingByIdOwner_Success() {
        List<Parking> parkings = Arrays.asList(new Parking(), new Parking());
        when(parkingDAOMongo.findParkingByIdOwner(anyString())).thenReturn(parkings);

        Response response = parkingRestController.getParkingByIdOwner("owner123");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(parkings, response.getEntity());
    }

    @Test
    public void testGetParkingByIdOwner_NotFound() {
        when(parkingDAOMongo.findParkingByIdOwner(anyString())).thenThrow(new NoSuchElementException());

        Response response = parkingRestController.getParkingByIdOwner("owner123");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun parcheggio riferito a questo id owner", response.getEntity());
    }

    @Test
    public void testGetAllParkings_Success() {
        List<Parking> parkings = Arrays.asList(new Parking(), new Parking());
        when(parkingDAOMongo.getAllParking()).thenReturn(parkings);

        Response response = parkingRestController.getAllParkings();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(parkings, response.getEntity());
    }

    @Test
    public void testGetAllParkings_NotFound() {
        when(parkingDAOMongo.getAllParking()).thenThrow(new NoSuchElementException());

        Response response = parkingRestController.getAllParkings();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun parcheggio", response.getEntity());
    }

    @Test
    public void testDeleteParkingById_Success() throws IOException {
        when(parkingDAOMongo.deleteParkingById(anyString())).thenReturn(true);

        Response response = parkingRestController.deleteParkingById("123");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Parcheggio eliminato", response.getEntity());
    }

    @Test
    public void testDeleteParkingById_NotFound() throws IOException {
        when(parkingDAOMongo.deleteParkingById(anyString())).thenReturn(false);

        Response response = parkingRestController.deleteParkingById("123");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Parcheggio non trovato", response.getEntity());
    }

    @Test
    public void testDeleteParkingById_Conflict() throws IOException {
        doThrow(new IllegalStateException()).when(parkingDAOMongo).deleteParkingById(anyString());

        Response response = parkingRestController.deleteParkingById("123");

        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
        assertEquals("Errore nel database: null", response.getEntity());
    }

    @Test
    public void testUpdateParking_Success() {
        when(parkingDAOMongo.updateParking(anyString(), any(Parking.class))).thenReturn(true);

        Parking parking = new Parking();
        Response response = parkingRestController.updateParking("123", parking);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Parking modificato con successo", response.getEntity());
    }

    @Test
    public void testUpdateParking_NotFound() {
        when(parkingDAOMongo.updateParking(anyString(), any(Parking.class))).thenReturn(false);

        Parking parking = new Parking();
        Response response = parkingRestController.updateParking("123", parking);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Parking non trovato o aggiornamento non riuscito", response.getEntity());
    }

    @Test
    public void testUpdateParking_BadRequest() {
        doThrow(new IllegalArgumentException("Errore di input")).when(parkingDAOMongo).updateParking(anyString(), any(Parking.class));

        Parking parking = new Parking();
        Response response = parkingRestController.updateParking("123", parking);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Errore: Errore di input", response.getEntity());
    }

}

