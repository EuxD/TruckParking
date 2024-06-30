package it.unisannio.ingsw24.parking;


import it.unisannio.ingsw24.parking.controller.ParkingRestController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;


@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class ApplicationParking extends ResourceConfig {
    public static void main( String[] args )
    {
        SpringApplication.run(ApplicationParking.class);
    }

    public ApplicationParking() { register(ParkingRestController.class);}
}
