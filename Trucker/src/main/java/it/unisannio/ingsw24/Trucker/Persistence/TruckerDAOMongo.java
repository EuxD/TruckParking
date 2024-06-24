package it.unisannio.ingsw24.Trucker.Persistence;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import it.unisannio.ingsw24.Trucker.DTO.TruckerLoginDTO;
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
    private static final String PREFIX = "U";
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
        if(resourcheEmail(t.getEmail())){
            int newSeq = getNextSequence();
            String newId = formatId(newSeq);
            t.setId_trucker(newId);
            try {
                Document trucker = truckerToDocument(t);
                collection.insertOne(trucker);
                return t;
            }catch (MongoWriteException e){
                e.printStackTrace();
            }
        }

        return null;
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

    private boolean resourcheEmail(String email){
        Document doc = this.collection.find(eq(ELEMENT_EMAIL, email)).first();
        if(doc == null){
            return true;
        }

        return false;
    }

//    @Override
//    public TruckerLoginDTO loginTrucker(TruckerLoginDTO t) {
//        Document doc = this.collection.find(and(
//                eq(ELEMENT_EMAIL, t.getEmail()),
//                eq(ELEMENT_PASSWORD, t.getPassword())
//        )).first();
//
//        TruckerLoginDTO truckerLoginDTO = truckerFromDocumentLogin(doc);
//
//        if(truckerLoginDTO == null){
//            return null;
//        } else{
//            return truckerLoginDTO;
//        }
//
//
//    }


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


}
