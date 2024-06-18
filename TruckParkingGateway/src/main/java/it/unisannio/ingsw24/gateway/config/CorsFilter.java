package it.unisannio.ingsw24.gateway.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addIntHeader("Access-Control-Max-Age", 10);
        filterChain.doFilter(request, response);
    }
}

/*
Il filtro CorsFilter che hai postato è una classe Java annotata con @Component, il che significa che viene gestita dal contenitore di Spring come un componente del framework. Questa classe estende OncePerRequestFilter, il che implica che il filtro verrà eseguito una volta per ogni richiesta HTTP.

Il filtro è progettato per gestire le impostazioni CORS (Cross-Origin Resource Sharing). CORS è un meccanismo di sicurezza che consente a una pagina web di richiedere risorse da un dominio diverso da quello che ha servito la pagina web. Vediamo cosa fa ogni riga del metodo doFilterInternal:

        response.addHeader("Access-Control-Allow-Origin", "*");: Permette l'accesso da qualsiasi origine. L'asterisco (*) indica che tutte le origini sono autorizzate. Questo è utile per il sviluppo, ma in produzione sarebbe meglio restringere l'accesso alle origini specifiche.

        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD");: Specifica i metodi HTTP consentiti quando si effettuano richieste cross-origin. In questo caso, i metodi permessi sono GET, POST, DELETE, PUT, PATCH e HEAD.

        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");: Elenca gli header che possono essere utilizzati nella richiesta effettiva effettuata dal client dopo la richiesta preflight.

        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");: Indica quali header sono sicuri da esporre al client.

        response.addHeader("Access-Control-Allow-Credentials", "true");: Permette l'invio di cookie e altre credenziali insieme alle richieste cross-origin. Quando è impostato su true, le richieste con credenziali sono consentite solo se l'origine specificata in Access-Control-Allow-Origin non è un asterisco (*).

        response.addIntHeader("Access-Control-Max-Age", 10);: Specifica per quanto tempo il risultato della richiesta preflight può essere memorizzato nella cache dal client. In questo caso, è impostato a 10 secondi.

        filterChain.doFilter(request, response);: Continua con l'esecuzione della catena di filtri. Questo metodo fa sì che la richiesta e la risposta passino al prossimo filtro o risorsa nella catena.

        In sintesi, questo filtro configura le intestazioni della risposta HTTP per gestire le richieste CORS, permettendo che siano effettuate richieste cross-origin con vari metodi HTTP e specifici header. Questo è utile per permettere a front-end ospitati su domini diversi di interagire con il server back-end.

*/
