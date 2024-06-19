package it.unisannio.ingsw24.gateway;

import it.unisannio.ingsw24.gateway.presentation.GatewayRestController;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@ApplicationPath("truckparking")
@SpringBootApplication
public class GatewayApplication extends ResourceConfig{

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    public GatewayApplication() {
        register(GatewayRestController.class);
    }

}


