package it.unisannio.ingsw24.Owner.Persistence;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class OwnerDAOMongo implements OwnerDAO{

    private String host = System.getenv("HOST");
    private String port = System.getenv("PORT");
    private static String URI;

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;
    private static OwnerDAOMongo ownerDAOMongo = null;
    private static final String COUNTER_ID = "counter";
    private static final String PREFIX = "U";
    private static final int ID_LENGTH = 2;

    public static OwnerDAOMongo getIstance(){
        if(ownerDAOMongo == null){
            ownerDAOMongo = new OwnerDAOMongo();
        }
        return ownerDAOMongo;
    }

    public OwnerDAOMongo(){
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

    private static Trucker ownerFromDocument(Document document){
        return new Trucker(document.getString(ELEMENT_ID),
                document.getString(ELEMENT_NAME),
                document.getString(ELEMENT_SURNAME),
                document.getDate(ELEMENT_BDATE),
                document.getString(ELEMENT_EMAIL),
                document.getString(ELEMENT_GENDER),
                document.getString(ELEMENT_ROLE),
                document.getString(ELEMENT_PASSWORD),
                document.getList(ELEMENT_PARKS, String.class));
    }

    private static Document ownerToDocument(Owner ow) {
        return new Document(ELEMENT_ID, ow.getId_owner())
                .append(ELEMENT_NAME, ow.getName())
                .append(ELEMENT_SURNAME, ow.getSurname())
                .append(ELEMENT_BDATE, ow.getbDate())
                .append(ELEMENT_EMAIL, ow.getEmail())
                .append(ELEMENT_GENDER, ow.getGender())
                .append(ELEMENT_ROLE, ow.getRole())
                .append(ELEMENT_PASSWORD, ow.getPassword())
                .append(ELEMENT_PARKS, ow.getParks());
    }

    @Override
    public Owner createOwner(Owner ow){
//        String newId = UUID.randomUUID().toString();
        if (resourcheEmail(ow.getEmail())) {
            int newSeq = getNextSequence();
            String newId = formatId(newSeq);
            ow.setId_owner(newId);
            try {
                Document trucker = ownerToDocument(ow);
                collection.insertOne(trucker);
                return ow;
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



}
