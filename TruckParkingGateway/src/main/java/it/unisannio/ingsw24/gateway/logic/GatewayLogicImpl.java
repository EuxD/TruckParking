package it.unisannio.ingsw24.gateway.logic;

import com.google.gson.*;
import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Parking.Parking;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import okhttp3.*;

import java.io.IOException;



public class GatewayLogicImpl implements GatewayLogic{

    private final String ownerAddress;
    private final String truckerAddress;
//    private final Gson gson;


    public GatewayLogicImpl(){
        String ownerHost = System.getenv("OWNER_HOST");
        String ownerPort = System.getenv("OWNER_PORT");

        if(ownerHost == null){
            ownerHost = "localhost";
        }

        if(ownerPort == null){
            ownerPort = "8082";
        }
        ownerAddress = "http://" + ownerHost + ":" + ownerPort;

        String truckerHost = System.getenv("TRUCKER_HOST");
        String truckerPort = System.getenv("TRUCKER_PORT");

        if(truckerHost == null){
            truckerHost = "localhost";
        }

        if(truckerPort == null){
            truckerPort = "8081";
        }
        truckerAddress = "http://" + truckerHost + ":" + truckerPort;
    }


    //////////////////////////////////// TRUCKER ///////////////////////////////////////////////////

    @Override
    public Trucker createTrucker(Trucker trucker) throws IOException{

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        String jsonBody = gson.toJson(trucker);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8081/trucker/create")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        System.out.println(request.body());
        Response response = client.newCall(request).execute();
        System.out.println(response.code());
        if (response.code() != 201) {
            return null;
        }

        return trucker;
    } //FUNZIONA

    @Override
    public Trucker getTruckerByEmail(String email) {
        try{
            String URL = String.format(truckerAddress + "/trucker/" + email);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200) {
                return null;
            }


            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
            String body = response.body().string();
//            System.out.println(body);
            Trucker t = gson.fromJson(body, Trucker.class);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    } //FUNZIONA

    @Override
    public Trucker getTruckerByID(String id) {
        try{
            String URL = String.format(truckerAddress + "/trucker/truckerID/" + id);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200) {
                return null;
            }


            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
            String body = response.body().string();
//            System.out.println(body);
            Trucker t = gson.fromJson(body, Trucker.class);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    } //FUNZIONA

    @Override
    public Trucker updateTrucker(Trucker trucker) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        String jsonBody = gson.toJson(trucker);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8081/trucker/update")
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
//        System.out.println(response.code());
        if (response.code() != 200) {
            return null;
        }

        return trucker;
    } //FUNZIONA

    @Override
    public Boolean deleteTruckerByEmail(String email) {
        try{
            String URL = String.format(truckerAddress + "/trucker/deleteTrucker?email=" + email);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200) {
                return false;
            }


        } catch (Exception e) {}

        return true;
    } //FUNZIONA

    @Override
    public Boolean deleteTruckerByID(String id) {
        try{
            String URL = String.format(truckerAddress + "/trucker/deleteID/" + id);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200) {
                return false;
            }


        } catch (Exception e) {}

        return true;
    } //FUNZIONA

    ///////////////////////////////////////////////// OWNER /////////////////////////////////////////////////

    @Override
    public Owner createOwner(Owner owner) throws IOException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        String jsonBody = gson.toJson(owner);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8082/owner/create")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.code());
        if (response.code() != 201) {
            return null;
        }

        return owner;
    }

    public Owner getOwnerByEmail(String email) {
        try {
            String URL = String.format(ownerAddress + "/owner/" + email);
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

    @Override
    public Owner getOwnerById(String id) throws IOException {
        try {
            String URL = String.format(ownerAddress + "/owner/ID/" + id);
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

    @Override
    public Boolean deleteOwnerByEmail(String email) throws IOException {
        try{
            String URL = String.format(ownerAddress + "/owner/delete/" + email);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200) {
                return false;
            }


        } catch (Exception e) {}

        return true;
    }

    @Override
    public Boolean deleteOwnerByID(String id) throws IOException {
        try{
            String URL = String.format(ownerAddress + "/owner/deleteID/" + id);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code() != 200) {
                return false;
            }


        } catch (Exception e) {}

        return true;
    }

    @Override
    public Owner updateOwner(Owner owner) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        String jsonBody = gson.toJson(owner);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8082/owner/update")
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
//        System.out.println(response.code());
        if (response.code() != 200) {
            return null;
        }

        return owner;
    }

    //////////////////////////////////// PARKING //////////////////////////////////////////

    @Override
    public Parking createParking(Parking parking) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Formatta la data nel formato desiderato
        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(parking);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8083/parking/create")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
//        System.out.println(response.code());
        if (response.code() != 201) {
            return null;
        }

        return parking;
    }





}