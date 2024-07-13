package it.unisannio.ingsw24.Owner.Persistence;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Owner.utils.EmailAlreadyExistsException;
import it.unisannio.ingsw24.Owner.utils.IllegalBDateException;

import org.bson.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static com.mongodb.client.model.Filters.and;
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
    private static final String PREFIX = "Ow";
    private static final int ID_LENGTH = 2;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


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

    private static Owner ownerFromDocument(Document document){

        return new Owner(document.getString(ELEMENT_ID),
                document.getString(ELEMENT_NAME),
                document.getString(ELEMENT_SURNAME),
                LocalDate.parse(document.getString(ELEMENT_BDATE),FORMATTER),
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
                .append(ELEMENT_BDATE, ow.getbDate().format(FORMATTER))
                .append(ELEMENT_EMAIL, ow.getEmail())
                .append(ELEMENT_GENDER, ow.getGender())
                .append(ELEMENT_ROLE, ow.getRole())
                .append(ELEMENT_PASSWORD, ow.getPassword())
                .append(ELEMENT_PARKS, ow.getParks());
    }

    private boolean checkbDate(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }
        if(dateOfBirth.isAfter(LocalDate.now())){
            return false;
        }
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        return age >= 18;
    }


    @Override
    public Owner createOwner(Owner ow){
        try {
            if (!checkbDate(ow.getbDate())) {
                throw new IllegalBDateException("Data di nascita non valida");
            }

            int newSeq = getNextSequence();
            String newId = formatId(newSeq);
            ow.setId_owner(newId);
            ow.setRole("ROLE_OWNER");

            Document owner = ownerToDocument(ow);
            collection.insertOne(owner);
            return ow;

        } catch (MongoWriteException e) {
            // Verifica se l'eccezione è causata da una violazione dell'unicità
            if (e.getCode() == 11000) {
                // Codice errore 11000 indica una violazione dell'indice unico
                throw new EmailAlreadyExistsException("Esiste già un account legato a questa mail: " + ow.getEmail());
            } else {
                // Stampa l'eccezione per altri tipi di errore di scrittura
                e.printStackTrace();
            }
        }
        return null;

    }

    @Override
    public Owner findOwnerByEmail(String email) {
        List<Owner> owners = new ArrayList<>();

        for (Document doc : this.collection.find(eq(ELEMENT_EMAIL, email))){
            Owner o = ownerFromDocument(doc);
            owners.add(o);
        }

        if (owners.size() > 1) {
            throw new IllegalStateException();
        }

        if (owners.isEmpty()) {
            throw new NoSuchElementException();
        }

        assert owners.size() == 1;
        return owners.get(0);
    }

    @Override
    public Owner findOwnerById(String id) {
        List<Owner> owners = new ArrayList<>();

        for (Document doc : this.collection.find(and(eq(ELEMENT_ID, id), eq(ELEMENT_ROLE,"ROLE_OWNER")))) {
            Owner o = ownerFromDocument(doc);
            owners.add(o);
        }

        if (owners.size() > 1) {
            throw new IllegalStateException();
        }

        if (owners.isEmpty()) {
            throw new NoSuchElementException();
        }

        assert owners.size() == 1;
        return owners.get(0);
    }

    @Override
    public Boolean deleteOwnerByEmail(String email) {
        List<Owner> owners = new ArrayList<>();

        for (Document doc : this.collection.find(and(eq(ELEMENT_EMAIL, email), eq(ELEMENT_ROLE, "ROLE_OWNER")))) {
            Owner ow = ownerFromDocument(doc);
            owners.add(ow);
        }

        if (owners.size() > 1) {
            throw new IllegalStateException();
        }

        if (owners.isEmpty()) {
            // Nessun trucker trovato con l'email specificata
            return false;
        }

        assert owners.size() == 1;
        this.collection.deleteOne(ownerToDocument(owners.get(0)));
        return true;
    }

    @Override
    public Boolean deleteOwnerByID(String id) {
        List<Owner> owners = new ArrayList<>();

        for (Document doc : this.collection.find(eq(ELEMENT_ID, id))){
            Owner ow = ownerFromDocument(doc);
            owners.add(ow);
        }

        if (owners.size() > 1) {
            throw new IllegalStateException();
        }

        if (owners.isEmpty()) {
            // Nessun trucker trovato con l'id specificato
            return false;
        }

        assert owners.size() == 1;
        this.collection.deleteOne(ownerToDocument(owners.get(0)));
        return true;
    }

    @Override
    public Owner updateOwner(String email, Owner ow) {
        try {
            // Crea la query per trovare l'owner con l'email specificata e il ruolo "ROLE_OWNER"
            Document query = new Document("$and", Arrays.asList(
                    new Document(ELEMENT_EMAIL, email),
                    new Document(ELEMENT_ROLE, "ROLE_OWNER")
            ));

            // Prepara i campi da aggiornare
            Document doc = new Document();
            if (ow.getName() != null) {
                doc.append(ELEMENT_NAME, ow.getName());
            }
            if (ow.getSurname() != null) {
                doc.append(ELEMENT_SURNAME, ow.getSurname());
            }
            if (ow.getPassword() != null) {
                doc.append(ELEMENT_PASSWORD, ow.getPassword());
            }

            if(ow.getParks() != null){
                doc.append(ELEMENT_PARKS, ow.getParks());
            }

            if (!doc.isEmpty()) {
                Document update = new Document("$set", doc);
                UpdateResult result = this.collection.updateOne(query, update);

                // Verifica se l'aggiornamento è andato a buon fine
                if (result.getMatchedCount() == 0) {
                    return null; // Nessun documento trovato o aggiornato
                }
            } else {
                // Nessun campo da aggiornare, lancia un'eccezione
                throw new IllegalArgumentException("Nessun campo valido da aggiornare fornito.");
            }

            return ow;

        } catch (MongoWriteException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante l'aggiornamento dell'owner: " + e.getMessage());
        }
    }








}
