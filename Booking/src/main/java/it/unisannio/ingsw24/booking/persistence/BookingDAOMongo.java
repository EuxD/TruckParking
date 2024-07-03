package it.unisannio.ingsw24.booking.persistence;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import it.unisannio.ingsw24.Entities.Booking.Booking;
import org.bson.Document;

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
    private static final String PREFIX = "U";
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


    @Override
    public Booking createBooking(Booking booking) {
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
