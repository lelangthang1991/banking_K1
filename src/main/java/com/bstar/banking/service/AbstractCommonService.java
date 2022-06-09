package com.bstar.banking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.bstar.banking.common.ExceptionString.INVALID_CREDENTIAL;
import static com.bstar.banking.common.ExceptionString.USER_DISABLED;

@RequiredArgsConstructor
@Service
public abstract class AbstractCommonService {
    private final JavaMailSender sender;
    private final AuthenticationManager authenticationManager;

    public void authenticate(String email, String password) throws Exception {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (DisabledException e) {
            throw new Exception(USER_DISABLED, e);
        } catch (BadCredentialsException e) {
            throw new Exception(INVALID_CREDENTIAL, e);
        }
    }
}
