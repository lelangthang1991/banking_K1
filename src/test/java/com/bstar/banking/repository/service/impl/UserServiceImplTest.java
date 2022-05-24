package com.bstar.banking.repository.service.impl;

import com.bstar.banking.jwt.JwtUtil;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.security.UserDetailsServiceImpl;

import com.bstar.banking.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
//        userService = new UserServiceImpl(userRepository, jwtUtil,  userDetailsService, authenticationManager);
    }

    @Test
    void generateTokenAndRefreshToken() throws Exception {
//        LoginDTO loginRequest = LoginDTO.builder().email("hoanganh25022000@gmail.com").password("123").build();
//        UserDetails userDetails = userDetailsService.loadUserByUsername("hoanganh25022000@gmail.com");
//        when(userDetailsService.loadUserByUsername("hoanganh25022000@gmail.com")).thenReturn(userDetails);
//        when(jwtUtil.generateToken(userDetails)).thenReturn("");
//        when(jwtUtil.generateRefreshToken(userDetails)).thenReturn("");
//
//        ObjectEvent objectEvent = userService.generateTokenAndRefreshToken(loginRequest);
//        System.out.println(objectEvent);
//        assertThat(objectEvent).isNotNull();
    }
}