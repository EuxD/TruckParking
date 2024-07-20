package it.unisannio.ingsw24.parking;

import it.unisannio.ingsw24.Entities.Parking.Parking;
import it.unisannio.ingsw24.parking.controller.ParkingRestController;
import it.unisannio.ingsw24.parking.persistence.ParkingDAOMongo;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    void testCreateParking_Success() {
        Parking parking = new Parking();
        when(parkingDAOMongo.createParking(any(Parking.class))).thenReturn(parking);

        Response response = parkingRestController.createParking(parking);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(parking, response.getEntity());
    }

    @Test
    void testCreateParking_Failure() {
        when(parkingDAOMongo.createParking(any(Parking.class))).thenReturn(null);

        Response response = parkingRestController.createParking(new Parking());

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Errore nella creazione del parcheggio", response.getEntity());
    }

    @Test
    void testGetParkingById_Success() {
        Parking parking = new Parking();
        when(parkingDAOMongo.findParkingById(anyString())).thenReturn(parking);

        Response response = parkingRestController.getParkingById("123");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(parking, response.getEntity());
    }

    @Test
    void testGetParkingById_NotFound() {
        when(parkingDAOMongo.findParkingById(anyString())).thenThrow(new NoSuchElementException());

        Response response = parkingRestController.getParkingById("123");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun Parking con quell'ID: 123", response.getEntity());
    }

    @Test
    void testGetParkingByIdOwner_Success() {
        List<Parking> parkings = Collections.singletonList(new Parking());
        when(parkingDAOMongo.findParkingByIdOwner(anyString())).thenReturn(parkings);

        Response response = parkingRestController.getParkingByIdOwner("owner123");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(parkings, response.getEntity());
    }

    @Test
    void testGetParkingByIdOwner_NotFound() {
        when(parkingDAOMongo.findParkingByIdOwner(anyString())).thenThrow(new NoSuchElementException());

        Response response = parkingRestController.getParkingByIdOwner("owner123");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun parcheggio riferito a questo id owner", response.getEntity());
    }

    @Test
    void testGetAllParkings_Success() {
        List<Parking> parkings = Collections.singletonList(new Parking());
        when(parkingDAOMongo.getAllParking()).thenReturn(parkings);

        Response response = parkingRestController.getAllParkings();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(parkings, response.getEntity());
    }

    @Test
    void testGetAllParkings_NotFound() {
        when(parkingDAOMongo.getAllParking()).thenThrow(new NoSuchElementException());

        Response response = parkingRestController.getAllParkings();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun parcheggio", response.getEntity());
    }

    @Test
    void testGetParkingByCity_Success() {
        List<Parking> parkings = Collections.singletonList(new Parking());
        when(parkingDAOMongo.getParkingByCity(anyString())).thenReturn(parkings);

        Response response = parkingRestController.getParkingByCity("CityName");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(parkings, response.getEntity());
    }

    @Test
    void testGetParkingByCity_NotFound() {
        when(parkingDAOMongo.getParkingByCity(anyString())).thenThrow(new NoSuchElementException());

        Response response = parkingRestController.getParkingByCity("CityName");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nessun Parking con quella città: CityName", response.getEntity());
    }

    @Test
    void testDeleteParkingById_Success() throws IOException {
        when(parkingDAOMongo.deleteParkingById(anyString())).thenReturn(true);

        Response response = parkingRestController.deleteParkingById("123");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Parcheggio eliminato", response.getEntity());
    }

    @Test
    void testDeleteParkingById_NotFound() throws IOException {
        when(parkingDAOMongo.deleteParkingById(anyString())).thenReturn(false);

        Response response = parkingRestController.deleteParkingById("123");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Parcheggio non trovato", response.getEntity());
    }

    @Test
    void testUpdateParking_Success() {
        Parking parking = new Parking();
        when(parkingDAOMongo.updateParking(anyString(), any(Parking.class))).thenReturn(true);

        Response response = parkingRestController.updateParking("123", parking);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Parking modificato con successo", response.getEntity());
    }

    @Test
    void testUpdateParking_NotFound() {
        Parking parking = new Parking();
        when(parkingDAOMongo.updateParking(anyString(), any(Parking.class))).thenReturn(false);

        Response response = parkingRestController.updateParking("123", parking);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Parking non trovato o aggiornamento non riuscito", response.getEntity());
    }
}
