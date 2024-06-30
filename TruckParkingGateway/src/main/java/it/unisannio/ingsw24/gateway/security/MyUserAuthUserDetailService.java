package it.unisannio.ingsw24.gateway.security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.user.AppUser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

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

        if (ownerHost == null) {
            ownerHost = "localhost";
        }

        if (ownerPort == null) {
            ownerPort = "8082";
        }
        ownerAddress = "http://" + ownerHost + ":" + ownerPort;

        String truckerHost = System.getenv("TRUCKER_HOST");
        String truckerPort = System.getenv("TRUCKER_PORT");

        if (truckerHost == null) {
            truckerHost = "localhost";
        }

        if (truckerPort == null) {
            truckerPort = "8081";
        }
        truckerAddress = "http://" + truckerHost + ":" + truckerPort;
    }

    private AppUser getUser(String email) {
        try {
            OkHttpClient client = new OkHttpClient();
            Gson gson = new GsonBuilder()
                    .setDateFormat("dd/MM/yyyy")
                    .create();

            // Controlla se l'utente è un trucker
            String truckerURL = String.format(truckerAddress + "/trucker/" + email);
            Request truckerRequest = new Request.Builder().url(truckerURL).get().build();
            Response truckerResponse = client.newCall(truckerRequest).execute();

            if (truckerResponse.isSuccessful()) {
                String truckerResponseBody = truckerResponse.body().string();
                Trucker trucker = gson.fromJson(truckerResponseBody, Trucker.class);
                return trucker;
            }

            // Se non è un trucker, controlla se l'utente è un owner
            String ownerURL = String.format(ownerAddress + "/owner/" + email);
            Request ownerRequest = new Request.Builder().url(ownerURL).get().build();
            Response ownerResponse = client.newCall(ownerRequest).execute();

            if (ownerResponse.isSuccessful()) {
                String ownerResponseBody = ownerResponse.body().string();
                Owner owner = gson.fromJson(ownerResponseBody, Owner.class);
                return owner;
            }

        } catch (IOException | JsonSyntaxException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Errore nella deserializzazione JSON", ex);
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("email= " + email);

        AppUser user = getUser(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}
