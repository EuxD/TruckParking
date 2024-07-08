package it.unisannio.ingsw24.gateway.logic;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.unisannio.ingsw24.Entities.Booking.Booking;
import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Parking.Parking;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import it.unisannio.ingsw24.gateway.utils.BookingCreateException;
import it.unisannio.ingsw24.gateway.utils.BookingNotFoundException;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


public class GatewayLogicImpl implements GatewayLogic{

    private final String ownerAddress;
    private final String truckerAddress;

    public GatewayLogicImpl(){
        String ownerHost = System.getenv("OWNER_HOST");
        String ownerPort = System.getenv("OWNER_PORT");

        if(ownerHost == null){
            ownerHost = "localhost";
        }

        if(ownerPort == null){
            ownerPort = "8082";
        }
        ownerAddress = "http://" + ownerHost + ":" + ownerPort;

        String truckerHost = System.getenv("TRUCKER_HOST");
        String truckerPort = System.getenv("TRUCKER_PORT");

        if(truckerHost == null){
            truckerHost = "localhost";
        }

        if(truckerPort == null){
            truckerPort = "8081";
        }
        truckerAddress = "http://" + truckerHost + ":" + truckerPort;
    }

//    @Override
//    public Boolean authenticateUser(String email, String pass) {
//        try {
//            // Ottieni l'utente da Owner o Trucker
//            Owner owner = getOwnerByEmail(email);
//            Trucker trucker = getTruckerByEmail(email);
//
//            // Confronta la password dell'Owner o del Trucker
//            if (owner != null) {
//                return pass.equals(owner.getPassword());
//            } else if (trucker != null) {
//                return pass.equals(trucker.getPassword());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }


    //////////////////////////////////// TRUCKER ///////////////////////////////////////////////////

    @Override
    public Trucker createTrucker(Trucker trucker) throws IOException{

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        String jsonBody = gson.toJson(trucker);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8081/trucker/create")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        System.out.println(request.body());
        Response response = client.newCall(request).execute();
        System.out.println(response.code());
        if (response.code() != 201) {
            return null;
        }

        return trucker;
    } //FUNZIONA

    @Override
    public Trucker getTruckerByEmail(String email) {
        try{
            String URL = String.format(truckerAddress + "/trucker/" + email);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200) {
                return null;
            }


            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
            String body = response.body().string();
//            System.out.println(body);
            Trucker t = gson.fromJson(body, Trucker.class);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    } //FUNZIONA

    @Override
    public Trucker getTruckerByID(String id) {
        try{
            String URL = String.format(truckerAddress + "/trucker/ID/" + id);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200) {
                return null;
            }


            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
            String body = response.body().string();
//            System.out.println(body);
            Trucker t = gson.fromJson(body, Trucker.class);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    } //FUNZIONA

    @Override
    public Trucker updateTrucker(String email, Trucker trucker) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        String jsonBody = gson.toJson(trucker);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8081/trucker/update" + email)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
//        System.out.println(response.code());
        if (response.code() != 200) {
            return null;
        }

        return trucker;
    } //FUNZIONA

    @Override
    public Boolean deleteTruckerByEmail(String email) {
        try{
            String URL = String.format(truckerAddress + "/trucker/delete/" + email);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200) {
                return false;
            }


        } catch (Exception e) {}

        return true;
    } //FUNZIONA

    @Override
    public Boolean deleteTruckerByID(String id) {
        try{
            String URL = String.format(truckerAddress + "/trucker/deleteID/" + id);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200) {
                return false;
            }


        } catch (Exception e) {}

        return true;
    } //FUNZIONA

    ///////////////////////////////////////////////// OWNER /////////////////////////////////////////////////

    @Override
    public Owner createOwner(Owner owner) throws IOException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        String jsonBody = gson.toJson(owner);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8082/owner/create")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.code());
        if (response.code() != 201) {
            return null;
        }

        return owner;
    }

    public Owner getOwnerByEmail(String email) {
        try {
            String URL = String.format(ownerAddress + "/owner/" + email);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200 ){
                return null;
            }
            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
            String body = response.body().string();
            Owner o = gson.fromJson(body, Owner.class);
            return o;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Owner getOwnerById(String id) throws IOException {
        try {
            String URL = String.format(ownerAddress + "/owner/ID/" + id);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200 ){
                return null;
            }
            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
            String body = response.body().string();
            Owner o = gson.fromJson(body, Owner.class);
            return o;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean deleteOwnerByEmail(String email) throws IOException {
        try{
            String URL = String.format(ownerAddress + "/owner/delete/" + email);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200) {
                return false;
            }


        } catch (Exception e) {}

        return true;
    }

    @Override
    public Boolean deleteOwnerByID(String id) throws IOException {
        try{
            String URL = String.format(ownerAddress + "/owner/deleteID/" + id);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200) {
                return false;
            }


        } catch (Exception e) {}

        return true;
    }

    @Override
    public Owner updateOwner(String email, Owner owner) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        String jsonBody = gson.toJson(owner);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8082/owner/update/" + email)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
//        System.out.println(response.code());
        if (response.code() != 200) {
            return null;
        }

        return owner;
    }

    //////////////////////////////////// PARKING //////////////////////////////////////////

    @Override
    public Parking createParking(Parking parking) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(parking);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8083/parking/create")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
//        System.out.println(response.code());
        if (response.code() != 201) {
            return null;
        }

        return parking;
    }

    @Override
    public Parking getParkingById(String id) {
        try {
            String URL = String.format(parkingAddress + "/parking/id/" + id);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200 ){
                return null;
            }
            Gson gson = new GsonBuilder().create();
            String body = response.body().string();
            Parking p = gson.fromJson(body, Parking.class);
            return p;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Parking> getParkingByIdOwner(String id) {
        try {
            String URL = String.format(parkingAddress + "/parking/idOwner/" + id);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                return null;
            }
            Gson gson = new GsonBuilder().create();
            String body = response.body().string();

            // Utilizzare TypeToken per deserializzare un array JSON
            Type listType = new TypeToken<List<Parking>>() {}.getType();
            List<Parking> parkings = gson.fromJson(body, listType);

            return parkings;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Parking> getAllParking() {
        try {
            String URL = String.format(parkingAddress + "/parking");
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                return null;
            }
            Gson gson = new GsonBuilder().create();
            String body = response.body().string();

            // Utilizzare TypeToken per deserializzare un array JSON
            Type listType = new TypeToken<List<Parking>>() {}.getType();
            List<Parking> parkings = gson.fromJson(body, listType);

            return parkings;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean deleteParkingById(String id) {
        try{
            String URL = String.format(parkingAddress + "/parking/delete/" + id);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200) {
                return false;
            }


        } catch (Exception e) {}

        return true;
    }

    @Override
    public Boolean updateParking(String id, Parking parking) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(parking);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8083/parking/update/" + id)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
//        System.out.println(response.code());
        if (response.code() != 200) {
            return false;
        }

        return true;
    }

//////////////////////////////////// BOOKING //////////////////////////////////////////


    @Override
    public Booking createBooking(Booking b){
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, context) ->
                        LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, type, context) ->
                        new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (json, type, context) ->
                        LocalTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("HH:mm")))
                .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (src, type, context) ->
                        new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("HH:mm"))))
                .create();
        String jsonBody = gson.toJson(b);

        try {
            RequestBody body = RequestBody.create(jsonBody, mediaType);
            Request request = new Request.Builder()
                    .url("http://localhost:8084/booking/create")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 201){
                throw new BookingCreateException("Fallimento nella creazione della prenotazione");
            }

            return b;

        } catch (IOException e){
            throw new BookingCreateException("Errore nella creazione della prenotazione");
        }
    }


    @Override
    public Booking getBookingById(String id) {
        try{
            String URL = String.format(bookingAddress + "/booking/ID/" + id);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200){
                throw new BookingNotFoundException("Nessuna prenotazione con questo ID: "+id);
            }
            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").setDateFormat("HH:mm").create();
            String body = response.body().toString();
            return gson.fromJson(body,Booking.class);
        } catch (IOException e){
            throw new RuntimeException("Errore nella richiesta di ricerca");
        }
    }

    @Override
    public List<Booking> getBookingByIdTrucker(String id_trucker) {
        try {
            String URL = String.format(bookingAddress + "/booking/truckerID/" + id_trucker);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                throw new BookingNotFoundException("Non ci sono prenotazioni legate a questo Trucker");
            }
            Gson gson = new GsonBuilder().create();
            String body = response.body().string();

            // Utilizzare TypeToken per deserializzare un array JSON
            Type listType = new TypeToken<List<Booking>>() {}.getType();
            List<Booking> bookings = gson.fromJson(body, listType);

            return bookings;
        } catch (IOException e) {
            throw new RuntimeException("Errore nella richiesta di ricerca");
        }
    }

    @Override
    public List<Booking> getBookingByIdParking(String id_parking) {
        try {
            String URL = String.format(bookingAddress + "/booking/parkingID/" + id_parking);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                throw new BookingNotFoundException("Nessuna prenotazione legata a questo parcheggio");
            }
            Gson gson = new GsonBuilder().create();
            String body = response.body().string();

            // Utilizzare TypeToken per deserializzare un array JSON
            Type listType = new TypeToken<List<Booking>>() {}.getType();
            List<Booking> bookings = gson.fromJson(body, listType);

            return bookings;
        } catch (IOException e) {
            throw new RuntimeException("Errore nella richiesta di ricerca");
        }
    }

    @Override
    public List<Booking> getAllBooking() {
        try {
            String URL = String.format(bookingAddress + "/booking");
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                throw new BookingNotFoundException("Non ci sono prenotazioni");
            }
            Gson gson = new GsonBuilder().create();
            String body = response.body().string();

            // Utilizzare TypeToken per deserializzare un array JSON
            Type listType = new TypeToken<List<Booking>>() {}.getType();
            List<Booking> bookings = gson.fromJson(body, listType);

            return bookings;
        } catch (IOException e) {
            throw new RuntimeException("Errore nella richiesta di ricerca delle prenotazioni");
        }
    }

    @Override
    public Boolean deleteBookingById(String id){
        try{
            String URL = String.format(bookingAddress + "/booking/delete/" + id);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200) {
                return false;
            }


        } catch (Exception e) {e.printStackTrace();}

        return true;
    }
}