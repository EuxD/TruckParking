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
        http.csrf().disable();
                http.authorizeHttpRequests()

                                .requestMatchers("/truckparking/rest/trucker/**").hasRole("TRUCKER")
                                .requestMatchers("/truckparking/rest/owner/**").hasRole("OWNER")
                                .requestMatchers("/truckparking/rest/parking/**").hasRole("OWNER")
                                .requestMatchers("/truckparking/rest/booking/**").hasRole("TRUCKER")
                                .anyRequest().authenticated().and().httpBasic();
//                )
//                .formLogin(formLogin ->
//                        formLogin
//                                .loginPage("/Accesso.html")  // Assicurati che questo URL sia corretto e accessibile
//                                .loginProcessingUrl("/Accesso.html")  // URL di invio della richiesta di login
//                                .defaultSuccessUrl("/HomePage.html", true)  // URL di successo del login
//                                .permitAll()
//                )
//                .csrf(csrf -> csrf.disable());

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
