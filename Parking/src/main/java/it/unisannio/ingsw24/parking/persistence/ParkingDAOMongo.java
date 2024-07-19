package it.unisannio.ingsw24.parking.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Parking.Parking;
import it.unisannio.ingsw24.parking.config.LocalDateAdapter;
import okhttp3.*;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.eq;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.ne;

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
                document.getDouble(ELEMENT_TARIFFA));
    }


    private static Document parkingToDocument(Parking p) {
        return new Document(ELEMENT_ID, p.getId_parking())
                .append(ELEMENT_ADDRESS, p.getAddress())
                .append(ELEMENT_CITY, p.getCity())
                .append(ELEMENT_ID_OWNER, p.getId_owner())
                .append(ELEMENT_PLACES, p.getnPlace())
                .append(ELEMENT_TARIFFA, p.getTariffa());
    }

    @Override
    public Parking createParking(Parking parking) {
        Owner o = checkIDOwner(parking.getId_owner());
        if(o == null){
            return null;
        }

        int newSeq = getNextSequence();
        String newId = formatId(newSeq);
        parking.setId_parking(newId);
        try {
            Document park = parkingToDocument(parking);
            collection.insertOne(park);
            addParkinginOwner(o,parking.getId_parking());
            return parking;
        } catch (MongoWriteException e) {
            e.printStackTrace();
        } catch (IOException e){
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
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();

            String body = response.body().string();
            System.out.println(body);
            Owner o = gson.fromJson(body, Owner.class);
            return o;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addParkinginOwner(Owner owner, String parkingId) throws IOException {
        if (owner.getParks() == null) {
            owner.setParks(new ArrayList<>());
        }
        owner.getParks().add(parkingId);
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String jsonBody = gson.toJson(owner);
        System.out.println(jsonBody);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8082/owner/update/" + owner.getEmail())
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            throw new IOException("Errore nell'aggiunta del parcheggio");
        }
    }

    private void removeParkingToOwner(Owner owner, String parkID) throws IOException {
        if (owner.getParks() == null || !owner.getParks().contains(parkID)) {
            throw new IOException("Parcheggio non trovato");
        }

        owner.getParks().remove(parkID);
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String jsonBody = gson.toJson(owner);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8082/owner/update/" + owner.getEmail())
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            throw new IOException("Errore nella rimozione della prenotazione");
        }
    }



    @Override
    public Parking findParkingById(String id) {
        List<Parking> park = new ArrayList<>();

        for(Document doc : this.collection.find(eq(ELEMENT_ID, id))){
            Parking p = parkingFromDocument(doc);
            Owner o = checkIDOwner(p.getId_owner()); // se owner eliminato, come se parcheggio fosse stato eliminato
            park.add(p);
        }

        if(park.size() > 1){
            throw new IllegalStateException();
        }

        if(park.isEmpty()){
            throw new NoSuchElementException();
        }

        assert park.size() == 1;
        return park.get(0);
    }

    @Override
    public Boolean deleteParkingById(String id) throws IOException {
        String id_owner = "";
        List<Parking> parkings = new ArrayList<>();

        for(Document doc : this.collection.find(eq(ELEMENT_ID, id))){
            Parking p = parkingFromDocument(doc);
            id_owner = p.getId_owner();
            parkings.add(p);
        }

        if(parkings.size() > 1){
            throw new IllegalStateException();
        }

        if(parkings.isEmpty()){
            return false;
        }

        assert parkings.size() == 1;
        Owner o = checkIDOwner(id_owner);
        removeParkingToOwner(o,id);
        this.collection.deleteOne(parkingToDocument(parkings.get(0)));
        return true;
    }

    @Override
    public List<Parking> findParkingByIdOwner(String id) {
        List<Parking> parkings = new ArrayList<>();

        for(Document doc : this.collection.find(eq(ELEMENT_ID_OWNER, id))){
            Parking p = parkingFromDocument(doc);
            parkings.add(p);
        }

        if(parkings.isEmpty()){
            throw new NoSuchElementException();
        }

        return parkings;

    }

    @Override
    public List<Parking> getAllParking(){
        List<Parking> parkings = new ArrayList<>();

        for(Document doc : this.collection.find(ne("_id", COUNTER_ID))){
            Parking p = parkingFromDocument(doc);
            parkings.add(p);
        }

        if(parkings.isEmpty()){
            throw new NoSuchElementException();
        }

        return parkings;

    }

    @Override
    public Boolean updateParking(String id, Parking parking) {
        try {
            if (parking.getnPlace() < 0) {
                return false;
            }

            if (parking.getTariffa() != null && parking.getTariffa() < 0) {
                return false;
            }

            Document query = new Document(ELEMENT_ID, id);
            Document doc = new Document();
            doc.append(ELEMENT_PLACES, parking.getnPlace()); // Include il campo nPlace anche se il valore è 0
            if (parking.getTariffa() != null) {
                doc.append(ELEMENT_TARIFFA, parking.getTariffa());
            }

            if (!doc.isEmpty()) {
                Document update = new Document("$set", doc);
                UpdateResult result = this.collection.updateOne(query, update);

                // Verifica se l'aggiornamento è andato a buon fine
                if (result.getMatchedCount() == 0) {
                    return false; // Nessun documento trovato o aggiornato
                }
            } else {
                // Nessun campo da aggiornare, lancia un'eccezione
                throw new IllegalArgumentException("Nessun campo valido da aggiornare fornito.");
            }

            return true;

        } catch (MongoWriteException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante l'aggiornamento del parking: " + e.getMessage());
        }
    }

    @Override
    public List<Parking> getParkingByCity(String city) {
        List<Parking> parkings = new ArrayList<>();

        for(Document doc : this.collection.find(eq(ELEMENT_CITY, city))){
            Parking p = parkingFromDocument(doc);
            parkings.add(p);
        }

        if(parkings.isEmpty()){
            throw new NoSuchElementException();
        }

        return parkings;
    }


}
