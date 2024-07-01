package it.unisannio.ingsw24.parking.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Parking.Parking;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.eq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ParkingDAOMongo implements ParkingDAO{

    private String host = System.getenv("HOST");
    private String port = System.getenv("PORT");
    private static String URI;

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;
    private static ParkingDAOMongo parkingDAOMongo = null;
    private static final String COUNTER_ID = "counter";
    private static final String PREFIX = "P";
    private static final int ID_LENGTH = 2;

    public static ParkingDAOMongo getIstance(){
        if(parkingDAOMongo == null){
            parkingDAOMongo = new ParkingDAOMongo();
        }
        return parkingDAOMongo;
    }

    public ParkingDAOMongo(){
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

    private static Parking parkingFromDocument(Document document){
        return new Parking(document.getString(ELEMENT_ID),
                document.getString(ELEMENT_ADDRESS),
                document.getString(ELEMENT_CITY),
                document.getString(ELEMENT_ID_OWNER),
                document.getInteger(ELEMENT_PLACES),
                document.getDouble(ELEMENT_RATE));
    }


    private static Document parkingToDocument(Parking p) {
        return new Document(ELEMENT_ID, p.getId_park())
                .append(ELEMENT_ADDRESS, p.getAddress())
                .append(ELEMENT_CITY, p.getCity())
                .append(ELEMENT_ID_OWNER, p.getId_owner())
                .append(ELEMENT_PLACES, p.getnPlace())
                .append(ELEMENT_RATE, p.getRate());
    }

    @Override
    public Parking createParking(Parking parking) {
        Owner o = checkIDOwner(parking.getId_owner());
        if(o == null || o.getRole() == "ROLE_TRUCKER"){
            return null;
        }

        int newSeq = getNextSequence();
        String newId = formatId(newSeq);
        parking.setId_park(newId);
        try {
            Document park = parkingToDocument(parking);
            collection.insertOne(park);
            return parking;
        } catch (MongoWriteException e) {
            e.printStackTrace();
        }

        return null;

    }

    private Owner checkIDOwner(String id) {
        try {
            String URL = String.format("http://localhost:8082/owner/ID/" + id);
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

    public Parking findParkingById(String id) {
        List<Parking> park = new ArrayList<>();

        for(Document doc : this.collection.find(eq(ELEMENT_ID, id))){
            Parking p = parkingFromDocument(doc);
            park.add(p);
        }

        if(park.isEmpty()){
            return null;
        }

        assert park.size() == 1;
        return park.get(0);
    }

    public Boolean deleteParkingById(String id){
        Document doc = this.collection.find(eq(ELEMENT_ID, id)).first();
        if(doc != null){
            this.collection.deleteOne(doc);
            return true;
        }
        return false;
    }

}
