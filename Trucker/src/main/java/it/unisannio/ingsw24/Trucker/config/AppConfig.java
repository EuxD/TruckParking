package it.unisannio.ingsw24.Trucker.config;

import it.unisannio.ingsw24.Trucker.Persistence.TruckerDAO;
import it.unisannio.ingsw24.Trucker.Persistence.TruckerDAOMongo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON) //garantisco che venga applicato il pattern singleton grazie ai BEAN di spring
    public TruckerDAO truckerDAO() {
        return new TruckerDAOMongo();
    }
}
