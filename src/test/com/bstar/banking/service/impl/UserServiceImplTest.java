package com.bstar.banking.service.impl;

import com.bstar.banking.entity.Session;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.jwt.AuthenticationHandler;
import com.bstar.banking.jwt.JwtUtil;
import com.bstar.banking.model.request.LoginDTO;
import com.bstar.banking.model.response.LoginResponse;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.SessionRepository;
import com.bstar.banking.repository.TransactionRepository;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.security.UserDetailsServiceImpl;
import com.bstar.banking.service.MailerService;
import com.bstar.banking.utils.DeviceType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

import static com.bstar.banking.common.JwtString.LOGIN_FAILURE;
import static com.bstar.banking.common.JwtString.LOGIN_SUCCESS;
import static com.bstar.banking.common.StatusCodeString.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private DeviceType deviceType;
    @Mock
    private HttpServletRequest request;
    @Mock
    private MailerService mailerService;
    @Mock
    private AuthenticationHandler authenticationHandler;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, transactionRepository, sessionRepository, jwtUtil,
                passwordEncoder, userDetailsService, modelMapper, deviceType, request, mailerService,
                authenticationHandler);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void givenLoginDTO_whenAuthenticationUserServiceJwtUtil_thenShouldReturnOKAndLoginSuccess() throws Exception {
        //given
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setEmail("hoanganh@gmail.com");
        loginRequest.setPassword("123");
        User user = new User();
        Session session = new Session();

        //when
        when(authenticationHandler.authenticate(Mockito.any(), Mockito.any())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(Mockito.mock(UserDetails.class));
        when(jwtUtil.generateToken(Mockito.any())).thenReturn("token");
        when(jwtUtil.getExpirationDateFromToken(Mockito.any())).thenReturn(Mockito.mock(Date.class));
        when(jwtUtil.generateRefreshToken(Mockito.any())).thenReturn("refresh-token");
        when(jwtUtil.getExpirationDateFromToken(Mockito.any())).thenReturn(Mockito.mock(Date.class));
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        when(request.getHeader(Mockito.any())).thenReturn("device");
        when(request.getHeader(Mockito.any())).thenReturn("device");
        when(deviceType.parseXForwardedHeader(Mockito.any())).thenReturn("device");
        when(sessionRepository.save(Mockito.any())).thenReturn(session);

        //then
        RestResponse<LoginResponse> response = userService.generateTokenAndRefreshToken(loginRequest);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(LOGIN_SUCCESS);
        Assertions.assertThat(response.getData()).isNotNull();

    }

    @Test
    void givenLoginDTO_whenAuthenticationUserServiceJwtUtilAndRemoteAddressIsNull_thenShouldReturnOKAndLoginSuccess() throws Exception {
        //given
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setEmail("hoanganh@gmail.com");
        loginRequest.setPassword("123");
        User user = new User();
        Session session = new Session();

        //when
        when(authenticationHandler.authenticate(Mockito.any(), Mockito.any())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(Mockito.mock(UserDetails.class));
        when(jwtUtil.generateToken(Mockito.any())).thenReturn("token");
        when(jwtUtil.getExpirationDateFromToken(Mockito.any())).thenReturn(Mockito.mock(Date.class));
        when(jwtUtil.generateRefreshToken(Mockito.any())).thenReturn("refresh-token");
        when(jwtUtil.getExpirationDateFromToken(Mockito.any())).thenReturn(Mockito.mock(Date.class));
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        when(request.getHeader(Mockito.any())).thenReturn("device");
        when(request.getHeader(Mockito.any())).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("device");
        when(sessionRepository.save(Mockito.any())).thenReturn(session);

        //then
        RestResponse<LoginResponse> response = userService.generateTokenAndRefreshToken(loginRequest);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(LOGIN_SUCCESS);
        Assertions.assertThat(response.getData()).isNotNull();
    }

    @Test
    void givenLoginDTO_whenAuthenticationUserServiceJwtUtilAndRemoteAddressIsNull_thenShouldReturnInvalidEmailOrPassword() throws Exception {
        //given
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setEmail("");
        loginRequest.setPassword("");
        User user = new User();
        Session session = new Session();

        //when
        when(authenticationHandler.authenticate(Mockito.any(), Mockito.any())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(Mockito.mock(UserDetails.class));
        when(jwtUtil.generateToken(Mockito.any())).thenReturn("token");
        when(jwtUtil.getExpirationDateFromToken(Mockito.any())).thenReturn(Mockito.mock(Date.class));
        when(jwtUtil.generateRefreshToken(Mockito.any())).thenReturn("refresh-token");
        when(jwtUtil.getExpirationDateFromToken(Mockito.any())).thenReturn(Mockito.mock(Date.class));
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        when(request.getHeader(Mockito.any())).thenReturn("device");
        when(request.getHeader(Mockito.any())).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("device");
        when(sessionRepository.save(Mockito.any())).thenReturn(session);

        //then
        RestResponse<LoginResponse> response = userService.generateTokenAndRefreshToken(loginRequest);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(LOGIN_SUCCESS);
        Assertions.assertThat(response.getData()).isNotNull();
    }

    @Test
    void givenLoginDTO_whenAuthenticationIsFalse_thenShouldReturnOKAndLoginFailure() throws Exception {
        //given
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setEmail("hoanganh@gmail.com");
        loginRequest.setPassword("123");

        //when
        when(authenticationHandler.authenticate(Mockito.any(), Mockito.any())).thenReturn(false);


        //then
        Throwable exception = assertThrows(NotFoundException.class,
                () -> userService.generateTokenAndRefreshToken(loginRequest));
        assertEquals(LOGIN_FAILURE, exception.getMessage());
    }

    @Test
    void forgotPassWord() {
    }

    @Test
    void signupUser() {
    }

    @Test
    void activateUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void infoUser() {
    }

    @Test
    void changePasswordByOldPassword() {
    }

    @Test
    void refreshToken() {
    }

    @Test
    void logout() {
    }

    @Test
    void findOneUser() {
    }

    @Test
    void findAllUserFiltered() {
    }

    @Test
    void userAdminCreate() {
    }

    @Test
    void userAdminUpdate() {
    }

    @Test
    void userAdminDisabled() {
    }

    @Test
    void userAdminDecentralization() {
    }

    @Test
    void findAllCardUser() {
    }
}