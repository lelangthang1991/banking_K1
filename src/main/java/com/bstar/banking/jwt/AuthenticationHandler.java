package com.bstar.banking.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.bstar.banking.common.ExceptionString.USER_DISABLED;
import static com.bstar.banking.common.JwtString.INCORRECT_EMAIL_OR_PASSWORD;

@Component
@RequiredArgsConstructor
public class AuthenticationHandler {
    private final AuthenticationManager authenticationManager;

    public boolean authenticate(String email, String password) throws Exception {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(auth);
            return true;
        } catch (DisabledException e) {
            throw new DisabledException(USER_DISABLED, e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(INCORRECT_EMAIL_OR_PASSWORD, e);
        }
    }
}
