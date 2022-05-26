package com.bstar.banking.security;


import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.entity.User;
import com.bstar.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static com.bstar.banking.common.UserString.GET_USER_EMAIL_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository customerDAO;

   @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = customerDAO.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
            return new UserDetailsImpl(user);
       } catch (Exception e) {
             throw new UsernameNotFoundException(GET_USER_EMAIL_NOT_FOUND + email);
        }
    }
}
