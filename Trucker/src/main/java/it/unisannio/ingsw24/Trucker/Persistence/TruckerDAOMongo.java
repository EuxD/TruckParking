package it.unisannio.ingsw24.Trucker.Persistence;

import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class TruckerDAOMongo implements TruckerDAO {

    private String host = System.getenv("HOST");
    private String port = System.getenv("PORT");
    private static String URI;

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;
    private static TruckerDAOMongo truckerDAO = null;
    private static final String COUNTER_ID = "counter";
    private static final String PREFIX = "T";
    private static final int ID_LENGTH = 2;

    public static TruckerDAOMongo getIstance(){
        if(truckerDAO == null){
            truckerDAO = new TruckerDAOMongo();
        }
        return truckerDAO;
    }

    public TruckerDAOMongo(){
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

    private static Trucker truckerFromDocument(Document document){
        return new Trucker(document.getString(ELEMENT_ID),
                document.getString(ELEMENT_NAME),
                document.getString(ELEMENT_SURNAME),
                document.getDate(ELEMENT_BDATE),
                document.getString(ELEMENT_EMAIL),
                document.getString(ELEMENT_GENDER),
                document.getString(ELEMENT_ROLE),
                document.getString(ELEMENT_PASSWORD),
                document.getList(ELEMENT_BOOKINGS, String.class));
    }


    private static Document truckerToDocument(Trucker t) {
        return new Document(ELEMENT_ID, t.getId_trucker())
                .append(ELEMENT_NAME, t.getName())
                .append(ELEMENT_SURNAME, t.getSurname())
                .append(ELEMENT_BDATE, t.getbDate())
                .append(ELEMENT_EMAIL, t.getEmail())
                .append(ELEMENT_GENDER, t.getGender())
                .append(ELEMENT_ROLE, t.getRole())
                .append(ELEMENT_PASSWORD, t.getPassword())
                .append(ELEMENT_BOOKINGS, t.getBookings());
    }

    public Trucker createTrucker(Trucker t){
//        String newId = UUID.randomUUID().toString();
        if (resourcheEmail(t.getEmail())) {
            int newSeq = getNextSequence();
            String newId = formatId(newSeq);
            t.setId_trucker(newId);
            try {
                Document trucker = truckerToDocument(t);
                collection.insertOne(trucker);
                return t;
            } catch (MongoWriteException e) {
                e.printStackTrace();
            }


        }
        return null;
    }

    private boolean resourcheEmail(String email) {
        Document doc = this.collection.find(eq(ELEMENT_EMAIL, email)).first();
        if (doc == null) {
            return true;
        }
        return false;
    }


    @Override
    public Trucker findTruckerById(String id) {
        List<Trucker> truckers = new ArrayList<>();

        for(Document doc : this.collection.find(eq(ELEMENT_ID, id))){
            Trucker t = truckerFromDocument(doc);
            truckers.add(t);
        }

        if(truckers.isEmpty()){
            return null;
        }

        assert truckers.size() == 1;
        return truckers.get(0);
    }


    @Override
    public Trucker findTruckerByEmail(String email) {
        List<Trucker> truckers = new ArrayList<>();

        for (Document doc : this.collection.find(eq(ELEMENT_EMAIL, email))) {
            Trucker t = truckerFromDocument(doc);
            truckers.add(t);
        }

        if (truckers.size() > 1) {
            throw new IllegalStateException();
        }

        if (truckers.isEmpty()) {
            throw new NoSuchElementException();
        }

        assert truckers.size() == 1;
        return truckers.get(0);
    }

    @Override
    public Trucker deleteTruckerByEmail(String email){
        if (!resourcheEmail(email)){
            Trucker deletedTrucker = getTruckerByEmail(email);
            try {
                collection.deleteOne(eq(ELEMENT_EMAIL, email));
                return deletedTrucker;
            } catch (MongoWriteException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public Trucker getTruckerByEmail(String email) {
        Trucker trucker = null;
        try {
            Document filter = new Document(ELEMENT_EMAIL, email);
            Document truckerDoc = collection.find(filter).first();
            if (truckerDoc != null) {
                trucker = documentToTrucker(truckerDoc);
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return trucker;
    }

//    private Trucker documentToTrucker(Document document) {
//        Trucker trucker = new Trucker();
//        trucker.setId_trucker(document.getObjectId("_id").toString());
//        trucker.setName(document.getString("name"));
//        trucker.setEmail(document.getString("email"));
//        // Add more fields as needed
//        return trucker;
//    }
private Trucker documentToTrucker(Document document) {
    Trucker trucker = new Trucker();
    trucker.setId_trucker(document.getString(ELEMENT_ID));
    trucker.setName(document.getString(ELEMENT_NAME));
    trucker.setSurname(document.getString(ELEMENT_SURNAME));
    trucker.setbDate(document.getDate(ELEMENT_BDATE));
    trucker.setEmail(document.getString(ELEMENT_EMAIL));
    trucker.setGender(document.getString(ELEMENT_GENDER));
    trucker.setRole(document.getString(ELEMENT_ROLE));
    trucker.setPassword(document.getString(ELEMENT_PASSWORD));
    return trucker;
}
}
