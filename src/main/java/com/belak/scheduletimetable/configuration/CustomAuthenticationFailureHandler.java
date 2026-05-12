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
        String error = "bad_credentials";
        if (exception instanceof UsernameNotFoundException) {
            error = "user_not_found";
        }
        else if (exception instanceof BadCredentialsException) {
            error = "bad_credentials";
        } else if (exception instanceof DisabledException) {
            error ="account_disabled";

        }
        response.sendRedirect("/login?error=" + error);
    }
}
