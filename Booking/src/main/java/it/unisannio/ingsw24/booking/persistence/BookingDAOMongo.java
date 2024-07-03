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
import org.bson.Document;
import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class BookingDAOMongo implements BookingDAO{
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

    public static BookingDAOMongo getIstance(){
        if(bookingDAOMongo == null){
            bookingDAOMongo = new BookingDAOMongo();
        }
        return bookingDAOMongo;
    }

    public BookingDAOMongo(){
        if(host == null){
            host = "172.31.6.11";
        }
        if(port == null){
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

    private static Booking bookingFromDocument(Document document){
        return new Booking(document.getString(ELEMENT_ID),
                document.getString(ELEMENT_ID_TRUCKER),
                document.getString(ELEMENT_ID_PARKING),
                document.getDate(ELEMENT_PDATE),
                document.getDate(ELEMENT_ORA_INIZIO),
                document.getDate(ELEMENT_ORA_FINE),
                document.getDouble(ELEMENT_TARIFFA));
    }

    private static Document bookingToDocument(Booking b) {
        return new Document(ELEMENT_ID, b.getId_booking())
                .append(ELEMENT_ID_TRUCKER, b.getId_trucker())
                .append(ELEMENT_ID_PARKING, b.getId_parking())
                .append(ELEMENT_PDATE, b.getpDate())
                .append(ELEMENT_ORA_INIZIO, b.getOra_inizio())
                .append(ELEMENT_ORA_FINE, b.getOra_fine())
                .append(ELEMENT_TARIFFA, b.getTariffa());
    }

    private Trucker checkIdTrucker(String id) throws IOException{
        try {
            String URL = String.format("http://localhost:8081/trucker/ID/" + id);
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
            Trucker t = gson.fromJson(body, Trucker.class);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Parking checkIdParking(String id) throws IOException{
        try {
            String URL = String.format("http://localhost:8083/parking/id/" + id);
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
    public Booking createBooking(Booking booking) throws IOException{
        Trucker t = checkIdTrucker(booking.getId_trucker());
        if(t == null){
            return null;
        }

        Parking p = checkIdParking(booking.getId_parking());
        if(p == null){
            return null;
        }
        int newSeq = getNextSequence();
        String newId = formatId(newSeq);
        booking.setId_booking(newId);
        try {
            Document b = bookingToDocument(booking);
            collection.insertOne(b);
            return booking;
        } catch (MongoWriteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Booking findBookingById(String id) {
        return null;
    }

    @Override
    public Booking deleteBookingById(String id) {
        return null;
    }
}
