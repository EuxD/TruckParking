package it.unisannio.ingsw24.Trucker.Persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import it.unisannio.ingsw24.Entities.Booking.Booking;
import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import it.unisannio.ingsw24.Trucker.utils.EmailAlreadyExistsException;
import it.unisannio.ingsw24.Trucker.utils.IllegalTruckerDeleteException;
import okhttp3.OkHttpClient;
import okhttp3.*;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Repository
public class TruckerDAOMongo implements TruckerDAO {

    private String host = System.getenv("HOST");
    private String port = System.getenv("PORT");
    private static String URI;

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;
    private static final String COUNTER_ID = "counter";
    private static final String PREFIX = "Tr";
    private static final int ID_LENGTH = 2;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public TruckerDAOMongo() {
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

    private static Trucker truckerFromDocument(Document document) {

        return new Trucker(document.getString(ELEMENT_ID),
                document.getString(ELEMENT_NAME),
                document.getString(ELEMENT_SURNAME),
                LocalDate.parse(document.getString(ELEMENT_BDATE),FORMATTER),
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
                .append(ELEMENT_BDATE, t.getbDate().format(FORMATTER))
                .append(ELEMENT_EMAIL, t.getEmail())
                .append(ELEMENT_GENDER, t.getGender())
                .append(ELEMENT_ROLE, t.getRole())
                .append(ELEMENT_PASSWORD, t.getPassword())
                .append(ELEMENT_BOOKINGS, t.getBookings());
    }

    private boolean checkbDate(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }
        if(dateOfBirth.isAfter(LocalDate.now())){
            return false;
        }
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        return age >= 20;
    }

    @Override
    public Trucker createTrucker(Trucker t) {
        try {
            if (!checkbDate(t.getbDate())) {
                throw new IllegalArgumentException("Data di nascita non valida");
            }

            int newSeq = getNextSequence();
            String newId = formatId(newSeq);
            t.setId_trucker(newId);
            t.setRole("ROLE_TRUCKER");

            Document trucker = truckerToDocument(t);
            collection.insertOne(trucker);
            return t;
        } catch (MongoWriteException e) {
            // Verifica se l'eccezione è causata da una violazione dell'unicità
            if (e.getCode() == 11000) {
                // Codice errore 11000 indica una violazione dell'indice unico
                throw new EmailAlreadyExistsException("Esiste già un account legato a questa mail: " + t.getEmail());
            } else {
                // Stampa l'eccezione per altri tipi di errore di scrittura
                e.printStackTrace();
            }
        }
        return null;

    }


    @Override
    public Trucker findTruckerById(String id) {
        List<Trucker> truckers = new ArrayList<>();

        for (Document doc : this.collection.find(eq(ELEMENT_ID, id))) {
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
    public Boolean deleteTruckerByID(String id) {
        List<Trucker> truckers = new ArrayList<>();

        for (Document doc : this.collection.find((eq(ELEMENT_ID, id)))) {
            Trucker t = truckerFromDocument(doc);
            truckers.add(t);
        }

        if (truckers.size() > 1) {
            throw new IllegalStateException();
        }

        if (truckers.isEmpty()) {
            // Nessun trucker trovato con l'id specificato
            return false;
        }

        if(!checkAreAllBookingsExpired(id)){
            throw new IllegalTruckerDeleteException("Impossibile eliminare il Trucker perchè ci sono delle prenotazioni in corso");
        }

        assert truckers.size() == 1;
        this.collection.deleteOne(truckerToDocument(truckers.get(0)));
        return true;
    }

    @Override
    public Trucker updateTrucker(String email, Trucker t) {
        try {
            // Crea la query per trovare l'owner con l'email specificata e il ruolo "ROLE_OWNER"
            Document query = new Document("$and", Arrays.asList(
                    new Document(ELEMENT_EMAIL, email),
                    new Document(ELEMENT_ROLE, "ROLE_TRUCKER")
            ));

            // Prepara i campi da aggiornare
            Document doc = new Document();
            if (t.getName() != null) {
                doc.append(ELEMENT_NAME, t.getName());
            }
            if (t.getSurname() != null) {
                doc.append(ELEMENT_SURNAME, t.getSurname());
            }
            if (t.getPassword() != null) {
                doc.append(ELEMENT_PASSWORD, t.getPassword());
            }

            if(t.getBookings() != null){
                doc.append(ELEMENT_BOOKINGS, t.getBookings());
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

            return t;

        } catch (MongoWriteException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante l'aggiornamento del trucker: " + e.getMessage());
        }
    }

    public Boolean checkAreAllBookingsExpired(String id_trucker){
        try {
            String URL = String.format("http://localhost:8084/booking/truckerID/" + id_trucker);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                return false;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }



}
