package it.unisannio.ingsw24.gateway.logic;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import it.unisannio.ingsw24.Entities.Booking.Booking;
import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Parking.Parking;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import it.unisannio.ingsw24.gateway.config.LocalDateAdapter;
import it.unisannio.ingsw24.gateway.config.LocalTimeAdapter;
import it.unisannio.ingsw24.gateway.presentation.EmailService;
import it.unisannio.ingsw24.gateway.utils.BookingCreateException;
import it.unisannio.ingsw24.gateway.utils.BookingNotFoundException;
import okhttp3.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class GatewayLogicImpl implements GatewayLogic{

    private final String ownerAddress;
    private final String truckerAddress;
    private final EmailService emailService;

    public GatewayLogicImpl(){
        String ownerHost = System.getenv("OWNER_HOST");
        String ownerPort = System.getenv("OWNER_PORT");

        emailService = new EmailService();

        if(ownerHost == null){
            ownerHost = "172.31.6.11";
        }

        if(ownerPort == null){
            ownerPort = "8082";
        }
        ownerAddress = "http://" + ownerHost + ":" + ownerPort;

        String truckerHost = System.getenv("TRUCKER_HOST");
        String truckerPort = System.getenv("TRUCKER_PORT");

        if(truckerHost == null){
            truckerHost = "172.31.6.11";
        }

        if(truckerPort == null){
            truckerPort = "8081";
        }
        truckerAddress = "http://" + truckerHost + ":" + truckerPort;
    }

    //////////////////////////////////// TRUCKER ///////////////////////////////////////////////////

    @Override
    public Trucker createTrucker(Trucker trucker) throws IOException{

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String jsonBody = gson.toJson(trucker);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://172.31.6.11:8081/trucker/create")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() != 201) {
            return null;
        }

        // Invio email di registrazione
        emailService.sendEmailRegistration(trucker.getEmail(),trucker.getName());

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


            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
            String body = response.body().string();
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


            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String jsonBody = gson.toJson(trucker);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://172.31.6.11:8081/trucker/update/" + email)
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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String jsonBody = gson.toJson(owner);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://172.31.6.11:8082/owner/create")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() != 201) {
            return null;
        }

        emailService.sendEmailRegistration(owner.getEmail(),owner.getName());

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
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
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
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String jsonBody = gson.toJson(owner);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://172.31.6.11:8082/owner/update/" + email)
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
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = authUser.getUsername();
        Owner ow = getOwnerByEmail(email);

        parking.setId_owner(ow.getId_owner());
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(parking);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://172.31.6.11:8083/parking/create")
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
                .url("http://172.31.6.11:8083/parking/update/" + id)
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

    @Override
    public List<Parking> getParkingByCity(String city) throws IOException {
        try {
            String URL = String.format(parkingAddress + "/parking/city/" + city);
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

    //////////////////////////////////// BOOKING //////////////////////////////////////////

    @Override
    public Booking createBooking(Booking b){
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = authUser.getUsername();
        Trucker tr = getTruckerByEmail(email);

        b.setId_trucker(tr.getId_trucker());
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();
        String jsonBody = gson.toJson(b);

        try {
            RequestBody body = RequestBody.create(jsonBody, mediaType);
            Request request = new Request.Builder()
                    .url("http://172.31.6.11:8084/booking/create")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 201){
                throw new BookingCreateException("Fallimento nella creazione della prenotazione");
            }

            // Deserialize the response to get the full Booking object
            String responseBody = response.body().string();
            Booking createdBooking = gson.fromJson(responseBody, Booking.class);

            // genero QR-CODE da inviare nella mail
            byte[] qrCodeData = qrCodeGenerate(createdBooking);

            // Invio email con QR-CODE
            Trucker t = getTruckerByID(createdBooking.getId_trucker());
            emailService.sendEmailWithAttachment(t.getEmail(),qrCodeData);



            return createdBooking;

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
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
            String body = response.body().string();
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
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
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
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
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
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
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

    //////////////////////////////////// EMAIL //////////////////////////////////////////

    private byte[] qrCodeGenerate(Booking b) {
        ByteArrayOutputStream qrCodeOutputStream = new ByteArrayOutputStream();
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String qrData = "ID_Booking: " + b.getId_booking()
                    + "\nData Prenotazione: " + b.getpDate()
                    + "\nOrario inizio prenotazione: " + b.getOra_inizio()
                    + "\nOrario fine: " + b.getOra_fine()
                    + "\nTotale: " + b.getTotal();

            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, 300, 300);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(bufferedImage, "PNG", qrCodeOutputStream);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        return qrCodeOutputStream.toByteArray();
    }

    @Override
    public List<Booking> getBookingProva(String id){
        try {
            String URL = String.format(parkingAddress + "/parking/infoBooking/" + id);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                return null;
            }
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
            String body = response.body().string();

            // Utilizzare TypeToken per deserializzare un array JSON
            Type listType = new TypeToken<List<Booking>>() {}.getType();
            List<Booking> bookings = gson.fromJson(body, listType);

            return bookings;
        } catch (IOException e) {
            throw new RuntimeException("Errore nella richiesta di ricerca");
        }
    }

}