package it.unisannio.ingsw24.gateway.security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserAuthUserDetailService implements UserDetailsService {

    private final String ownerAddress;
    private final String truckerAddress;

    public MyUserAuthUserDetailService() {
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

    public Trucker getTrucker(String email) {
        try {
            String URL = String.format(truckerAddress + "/trucker/" + email);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
//            System.out.println(responseBody); // Stampa per debug

            Gson gson = new GsonBuilder()
                    .setDateFormat("dd/MM/yyyy")
                    .create();
            Trucker trucker = gson.fromJson(responseBody, Trucker.class);
            return trucker;

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Errore nella deserializzazione JSON", ex);
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        System.out.println("email= " + mail);

        Trucker t = getTrucker(mail);
        if (t == null) {
            throw new UsernameNotFoundException("Trucker not found with email: " + mail);
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(t.getRole()));
        User user = new User(t.getEmail(), t.getPassword(), grantedAuthorities);
        return user;
    }
}
