package it.unisannio.ingsw24.Owner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class OwnerApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(OwnerApplication.class, args);
    }
}
