package com.manda.agenda.handlers;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationHandle implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // TODO Auto-generated method stub
        // Vérification des Roles
        System.out.println("Authorities222==================" + authentication.getAuthorities());
        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_FIRST_LOGIN"))) {
            response.sendRedirect("/first/formModifierPassword");
        } else {
            response.sendRedirect("/listeEvenement");
        }
    }

}
