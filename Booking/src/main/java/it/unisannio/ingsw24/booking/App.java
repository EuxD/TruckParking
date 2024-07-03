package it.unisannio.ingsw24.booking;

import it.unisannio.ingsw24.booking.Controller.BookingRestController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * Hello world!
 *
 */

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class App extends ResourceConfig
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class);
    }

    public App(){
        register(BookingRestController.class);
    }
}
