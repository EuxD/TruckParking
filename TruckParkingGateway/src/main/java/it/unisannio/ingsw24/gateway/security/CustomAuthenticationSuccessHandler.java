package it.unisannio.ingsw24.gateway.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/HomePage.html"; // Default redirect URL

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_TRUCKER"))) {
            redirectUrl = "/TruckerHomePage.html"; // Redirect URL for truckers
        } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_OWNER"))) {
            redirectUrl = "/OwnerHomePage.html"; // Redirect URL for owners
        }

        response.sendRedirect(redirectUrl);
    }
}
