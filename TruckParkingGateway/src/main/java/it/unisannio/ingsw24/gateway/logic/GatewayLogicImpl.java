package it.unisannio.ingsw24.gateway.logic;

import com.google.gson.*;
import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Trucker.DTO.TruckerLogin;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import okhttp3.*;

import javax.swing.text.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


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

    @Override
    public TruckerLogin truckerLogin(TruckerLogin truckerLogin) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(truckerLogin);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://localhost:8081/trucker/loginTrucker")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.code());
        if (response.code() != 200) {
            return null;
        }

        return truckerLogin;
    }


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
    }

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

    }


}