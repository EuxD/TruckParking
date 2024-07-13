package it.unisannio.ingsw24.booking.persistence;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import it.unisannio.ingsw24.Entities.Booking.Booking;
import it.unisannio.ingsw24.Entities.Parking.Parking;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import it.unisannio.ingsw24.booking.config.LocalDateAdapter;
import it.unisannio.ingsw24.booking.config.LocalTimeAdapter;
import org.bson.Document;
import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unisannio.ingsw24.booking.utils.BookingNotExpiredException;


import java.awt.print.Book;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.ne;

public class BookingDAOMongo implements BookingDAO {
    private String host = System.getenv("HOST");
    private String port = System.getenv("PORT");
    private static String URI;

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;
    private static BookingDAOMongo bookingDAOMongo = null;
    private static final String COUNTER_ID = "counter";
    private static final String PREFIX = "B";
    private static final int ID_LENGTH = 2;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");


    public static BookingDAOMongo getIstance() {
        if (bookingDAOMongo == null) {
            bookingDAOMongo = new BookingDAOMongo();
        }
        return bookingDAOMongo;
    }

    public BookingDAOMongo() {
        if (host == null) {
            host = "172.31.6.11";
        }
        if (port == null) {
            port = "27017";
        }
        URI = "mongodb://" + host + ":" + port;
        this.mongoClient = MongoClients.create(URI);
        this.database = mongoClient.getDatabase(DATABASE_NAME);
        this.collection = database.getCollection(COLLECTION_NAME);
        initializeCounter();
    }

    private void initializeCounter() {
        Document counter = collection.find(new Document("_id", COUNTER_ID)).first();
        if (counter == null) {
            collection.insertOne(new Document("_id", COUNTER_ID).append("seq", 0));
        }
    }

    private String formatId(int seq) {
        return String.format(PREFIX + "%0" + ID_LENGTH + "d", seq);
    }

    private int getNextSequence() {
        Document result = collection.findOneAndUpdate(
                new Document("_id", COUNTER_ID),
                Updates.inc("seq", 1)
        );
        return result.getInteger("seq") + 1;
    }

    private static Booking bookingFromDocument(Document document) {
        return new Booking(
                document.getString(ELEMENT_ID),
                document.getString(ELEMENT_ID_TRUCKER),
                document.getString(ELEMENT_ID_PARKING),
                LocalDate.parse(document.getString(ELEMENT_PDATE), FORMATTER),
                LocalTime.parse(document.getString(ELEMENT_ORA_INIZIO), TIME_FORMATTER),
                LocalTime.parse(document.getString(ELEMENT_ORA_FINE), TIME_FORMATTER),
                document.getDouble(ELEMENT_TOTALE)
        );
    }

    private static Document bookingToDocument(Booking b) {
        return new Document(ELEMENT_ID, b.getId_booking())
                .append(ELEMENT_ID_TRUCKER, b.getId_trucker())
                .append(ELEMENT_ID_PARKING, b.getId_parking())
                .append(ELEMENT_PDATE, b.getpDate().format(FORMATTER))
                .append(ELEMENT_ORA_INIZIO, b.getOra_inizio().format(TIME_FORMATTER))
                .append(ELEMENT_ORA_FINE, b.getOra_fine().format(TIME_FORMATTER))
                .append(ELEMENT_TOTALE, b.getTotal());
    }

    private Trucker checkIdTrucker(String id) throws IOException {
        try {
            String URL = String.format("http://localhost:8081/trucker/ID/" + id);
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
            Trucker t = gson.fromJson(body, Trucker.class);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Parking checkIdParking(String id) throws IOException {
        try {
            String URL = String.format("http://localhost:8083/parking/id/" + id);
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
            Parking p = gson.fromJson(body, Parking.class);
            return p;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addBookingToTrucker(Trucker trucker, String bookingId) throws IOException {
        if (trucker.getBookings() == null) {
            trucker.setBookings(new ArrayList<>());
        }

        trucker.getBookings().add(bookingId);
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();
        String jsonBody = gson.toJson(trucker);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8081/trucker/update/" + trucker.getEmail())
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            throw new IOException("Errore nell'aggiunta della prenotazione");
        }
    }

    private void removeBookingToTrucker(Trucker trucker, String boookingID) throws IOException {
        if (trucker.getBookings() == null || !trucker.getBookings().contains(boookingID)) {
            throw new IOException("Prenotazione non trovata");
        }

        trucker.getBookings().remove(boookingID);
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();
        String jsonBody = gson.toJson(trucker);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8081/trucker/update/" + trucker.getEmail())
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            throw new IOException("Errore nella rimozione della prenotazione");
        }
    }

    private void checkNPlaceParking(Booking b, Parking p) throws IOException {
        List<Booking> bookings = getBookingByIdParking(p.getId_parking());

        int countConflictingBookings = 0;
        for (Booking booking : bookings) {
            // Verifica se la prenotazione esistente è nello stesso giorno e se c'è sovrapposizione
            if (booking.getpDate().equals(b.getpDate()) &&
                    ((b.getOra_inizio().isBefore(booking.getOra_fine()) && b.getOra_fine().isAfter(booking.getOra_inizio())))) {
                countConflictingBookings++;
            }
        }

        // Verifica se il numero di prenotazioni in conflitto è maggiore o uguale ai posti disponibili
        if (countConflictingBookings >= p.getnPlace()) {
            throw new IllegalStateException("Non è più possibile effettuare prenotazioni per questo parcheggio.");
        }
    }

    private Double calcoloTariffaTotale(LocalTime ora_inizio, LocalTime ora_fine, String id_parking) throws IOException {
        Parking p = checkIdParking(id_parking);
        System.out.println(p.getTariffa());

        // Calcolo della differenza di tempo in minuti
        long durationInMinutes = java.time.Duration.between(ora_inizio, ora_fine).toMinutes();

        // Conversione della differenza di tempo in ore decimali
        double hours = durationInMinutes / 60.0;

        // Calcolo del totale da pagare
        return hours * p.getTariffa();
    }

    @Override
    public Booking createBooking(Booking booking) throws IOException {

        // Verifica che la data della prenotazione non sia precedente alla data odierna
        if (booking.getpDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La data della prenotazione non può essere antecedente alla data odierna.");
        }

        Trucker t = checkIdTrucker(booking.getId_trucker());
        if (t == null) {
            return null;
        }

        Parking p = checkIdParking(booking.getId_parking());
        if (p == null) {
            return null;
        }

        checkNPlaceParking(booking, p);

        Double totale = calcoloTariffaTotale(booking.getOra_inizio(), booking.getOra_fine(), booking.getId_parking());
        booking.setTotal(totale);

        String bookingId = formatId(getNextSequence());
        booking.setId_booking(bookingId);

        try {
            Document doc = bookingToDocument(booking);
            collection.insertOne(doc);
            addBookingToTrucker(t, bookingId);
        } catch (MongoWriteException e) {
            if (e.getError().getCategory().equals(com.mongodb.ErrorCategory.DUPLICATE_KEY)) {

                throw new IOException("Errore nella creazione della prenotazione: ID duplicato.");
            } else {
                throw new IOException("Errore nella creazione della prenotazione.");
            }
        }

        return booking;
    }

    @Override
    public Booking findBookingById(String id) {
        List<Booking> bookings = new ArrayList<>();

        for(Document doc : this.collection.find(eq(ELEMENT_ID, id))){
            Booking b = bookingFromDocument(doc);
            bookings.add(b);
        }

        if(bookings.size() > 1){
            throw new IllegalStateException();
        }

        if(bookings.isEmpty()){
            throw new NoSuchElementException();
        }

        assert bookings.size() == 1;
        return bookings.get(0);
    }

    @Override
    public List<Booking> getBookingByIdTrucker(String id_trucker) {
        List<Booking> bookings = new ArrayList<>();

        for (Document doc : this.collection.find(eq(ELEMENT_ID_TRUCKER, id_trucker))) {
            bookings.add(bookingFromDocument(doc));
        }

        if (bookings.isEmpty()) {
            throw new NoSuchElementException("Nessuna prenotazione trovata per il camionista con ID: " + id_trucker);
        }

        return bookings;
    }

    @Override
    public List<Booking> getBookingByIdParking(String id_parking) {
        System.out.println(id_parking);
        List<Booking> bookings = new ArrayList<>();

        for (Document doc : this.collection.find(eq(ELEMENT_ID_PARKING, id_parking))) {
            bookings.add(bookingFromDocument(doc));
        }

//        if(bookings.isEmpty()){
//            throw new NoSuchElementException();
//        }

        return bookings;
    }

    @Override
    public List<Booking> getAllBooking() {
        List<Booking> bookings = new ArrayList<>();

        for (Document doc : this.collection.find(ne("_id", COUNTER_ID))) {
            bookings.add(bookingFromDocument(doc));
        }

        if (bookings.isEmpty()) {
            throw new NoSuchElementException("Nessuna prenotazione trovata.");
        }

        return bookings;
    }


    @Override
    public Boolean deleteBookingById(String id) throws IOException {
        Document query = new Document(ELEMENT_ID, id);
        Document bookingDoc = collection.find(query).first();

        if (bookingDoc == null) {
            return false; // Prenotazione non trovata
        }

        Booking booking = bookingFromDocument(bookingDoc);
        Trucker trucker = checkIdTrucker(booking.getId_trucker());

        if (trucker == null) {
            return false; // Camionista non trovato
        }

        if (!isBookingExpired(booking)) {
            throw new BookingNotExpiredException("La prenotazione non è ancora scaduta, non puoi rimuoverla"); // Prenotazione non scaduta
        }

        try {
            collection.deleteOne(query);
            removeBookingToTrucker(trucker, id);
            return true; // Prenotazione scaduta cancellata con successo
        } catch (MongoWriteException e) {
            e.printStackTrace();
            throw new IOException("Errore nella rimozione della prenotazione.");
        }
    }

    @Override
    public Boolean areAllBookingsExpired(String truckerId){
        // Recupera tutte le prenotazioni per il camionista
        List<Booking> bookings = getBookingByIdTrucker(truckerId);

        // Verifica se tutte le prenotazioni sono scadute
        for (Booking booking : bookings) {
            if (!isBookingExpired(booking)) {
                throw new BookingNotExpiredException("Esiste almeno una prenotazione ancora attiva: "+ booking.getId_booking());
            }
        }
        return true; // Tutte le prenotazioni sono scadute
    }



    private boolean isBookingExpired(Booking booking) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bookingEndDateTime = LocalDateTime.of(booking.getpDate(), booking.getOra_fine());
        return bookingEndDateTime.isBefore(now);
    }

}


