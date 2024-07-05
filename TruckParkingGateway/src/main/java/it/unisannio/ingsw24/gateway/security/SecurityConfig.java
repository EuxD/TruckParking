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

//        http.authorizeHttpRequests().anyRequest().permitAll();

        http.authorizeRequests().requestMatchers("/truckparking/rest/trucker/**")
                .authenticated().and().httpBasic();

        http.authorizeRequests().requestMatchers("/HomePage.html").permitAll();
        http.authorizeRequests().requestMatchers("/Accesso.html").permitAll();
        http.authorizeRequests().requestMatchers("/ChiSiamo" +
                ".html").permitAll();


        return http.build();
    }


    public SecurityConfig(MyUserAuthUserDetailService userDetailService){
        this.userDetailService = userDetailService;
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
}
