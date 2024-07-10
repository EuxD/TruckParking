package it.unisannio.ingsw24.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class SecurityConfig {

    private final MyUserAuthUserDetailService userDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disabilita la protezione CSRF
        http.csrf().disable();

        // Configurazione delle autorizzazioni delle richieste
        http.authorizeHttpRequests()
                .requestMatchers("/truckparking/rest/trucker/delete/**").hasRole("TRUCKER")
                .requestMatchers("/truckparking/rest/trucker/update/**").hasRole("TRUCKER")
                .requestMatchers("/truckparking/rest/trucker/email/**").hasRole("TRUCKER")
                .requestMatchers("/truckparking/rest/booking/**").hasRole("TRUCKER")
                .requestMatchers("/truckparking/rest/trucker/ID/**").permitAll()

                .requestMatchers("/truckparking/rest/owner/**").hasRole("OWNER")
                .requestMatchers("/truckparking/rest/parking/**").hasRole("OWNER")

                .requestMatchers("viewTrucker.html").permitAll()
                .requestMatchers("HomePage.html").permitAll()
                .requestMatchers("Accesso.html").permitAll()
                .requestMatchers("ChiSiamo.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        return http.build();
    }



    @Bean
    public AuthenticationManager customAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoderPlain();
    }

    public SecurityConfig(MyUserAuthUserDetailService userDetailService){
        this.userDetailService = userDetailService;
    }
}
