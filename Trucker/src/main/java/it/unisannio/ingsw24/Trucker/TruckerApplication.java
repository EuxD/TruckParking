package it.unisannio.ingsw24.Trucker;

import it.unisannio.ingsw24.Trucker.Controller.TruckerRestController;
import jakarta.ws.rs.ApplicationPath;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class TruckerApplication  {

    public static void main(String[] args) {
        SpringApplication.run(TruckerApplication.class, args);
    }

//    public TruckerApplication(){
//        register(Endpoint.class);
//        setProperties(Collections.singletonMap("jersey.config.server.response.setStatusOverSendError", true));
//        register(RolesAllowedDynamicFeature.class);
//        register(TruckerRestController.class);
//
//
//    }
}