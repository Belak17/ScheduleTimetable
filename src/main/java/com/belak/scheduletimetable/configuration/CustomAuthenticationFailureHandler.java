package com.belak.scheduletimetable.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {

        // Déterminer le type d'erreur
        String error = "bad_credentials";

        if (exception instanceof UsernameNotFoundException) {
            error = "user_not_found";
        } else if (exception instanceof DisabledException) {
            error = "account_disabled";
        } else if (exception instanceof BadCredentialsException) {
            error = "bad_credentials";
        }

        // Identifier le type de connexion
        String loginType = request.getParameter("loginType");

        // Déterminer la page de redirection
        String redirectUrl = "professor".equals(loginType)
                ? "/login/professor?error=" + error
                : "/login?error=" + error;

        response.sendRedirect(
                request.getContextPath() + redirectUrl
        );
    }
}