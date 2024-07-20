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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class SecurityConfig {

    private final MyUserAuthUserDetailService userDetailService;

    public SecurityConfig(MyUserAuthUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeHttpRequests()
                .requestMatchers("/truckparking/rest/trucker/**").hasRole("TRUCKER")
                .requestMatchers("/truckparking/rest/owner/**").hasRole("OWNER")
                .requestMatchers("/truckparking/rest/parking/city/**").hasRole("TRUCKER")
                .requestMatchers("/truckparking/rest/parking/**").hasRole("OWNER")
                .requestMatchers("/truckparking/rest/booking/").hasRole("TRUCKER")
                .requestMatchers("/SearchParkingMap.html").hasRole("TRUCKER")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/Accesso.html")  // Path to your custom login page
                .loginProcessingUrl("/truckparking/rest/login")
                .successHandler(customAuthenticationSuccessHandler())
                .failureUrl("/Accesso.html?error=true");


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
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoderPlain();
    }
}
