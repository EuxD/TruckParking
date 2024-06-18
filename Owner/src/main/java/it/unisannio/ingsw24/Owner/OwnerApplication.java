package it.unisannio.ingsw24.Owner;

import it.unisannio.ingsw24.Owner.Controller.OwnerRestController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class OwnerApplication extends ResourceConfig
{
    public static void main( String[] args )
    {
        SpringApplication.run(OwnerApplication.class, args);
    }

    public OwnerApplication(){
        register(OwnerRestController.class);
    }
}
