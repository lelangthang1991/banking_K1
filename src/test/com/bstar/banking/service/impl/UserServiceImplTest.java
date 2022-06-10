package com.bstar.banking.service.impl;

import com.bstar.banking.entity.Card;
import com.bstar.banking.entity.Session;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.CompareException;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.jwt.AuthenticationHandler;
import com.bstar.banking.jwt.JwtUtil;
import com.bstar.banking.model.request.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.bstar.banking.common.JwtString.*;
import static com.bstar.banking.common.StatusCodeString.OK;
import static com.bstar.banking.common.UserString.*;
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
    void givenUserVerifyCodeAndForgotPassword_whenUserFindByIdAndSave_thenShouldReturnOkAndChangePasswordSuccess() {
        //given
        User user = new User();
        user.setVerifyCode("1234");
        ForgotPasswordDTO forgotPasswordDTO = new ForgotPasswordDTO();
        forgotPasswordDTO.setEmail("hoanganh@gmail.com");
        forgotPasswordDTO.setVerifyCode("1234");
        forgotPasswordDTO.setNewPassword("1234567");

        //when
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any())).thenReturn(Mockito.mock(User.class));

        //then
        RestResponse response = userService.forgotPassWord(forgotPasswordDTO);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(CHANGE_PASSWORD_SUCCESSFUL);
    }

    @Test
    void givenUserVerifyCodeAndForgotPassword_whenUserFindByIdAndSave_thenShouldThrowVerifyCodeDoesNotMatch() {
        //given
        User user = new User();
        user.setVerifyCode("1234");
        ForgotPasswordDTO forgotPasswordDTO = new ForgotPasswordDTO();
        forgotPasswordDTO.setEmail("hoanganh@gmail.com");
        forgotPasswordDTO.setVerifyCode("1111");
        forgotPasswordDTO.setNewPassword("1234567");

        //when
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        //then
        Throwable exception = assertThrows(CompareException.class,
                () -> userService.forgotPassWord(forgotPasswordDTO));
        assertEquals(VERIFY_CODE_DOES_NOT_MATCH, exception.getMessage());
    }

    @Test
    void givenUserVerifyCodeAndForgotPassword_whenUserFindByIdAndSave_thenShouldThrowNewPasswordCanNotBeTheSameAsTheOldOne() {
        //given
        User user = new User();
        user.setPassword("036200");
        user.setVerifyCode("1234");
        ForgotPasswordDTO forgotPasswordDTO = new ForgotPasswordDTO();
        forgotPasswordDTO.setEmail("hoanganh@gmail.com");
        forgotPasswordDTO.setVerifyCode("1234");
        forgotPasswordDTO.setNewPassword("036200");

        //when
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        //then
        Throwable exception = assertThrows(CompareException.class,
                () -> userService.forgotPassWord(forgotPasswordDTO));
        assertEquals(NEW_PASSWORD_CAN_NOT_BE_THE_SAME_AS_THE_OLD_ONE, exception.getMessage());

    }

    @Test
    void signupUser() {
    }

    @Test
    void givenUserEmailAndVerifyCode_thenGetUserByEmailAndSaveUser_whenShouldReturnOkAndCardActivateSuccessful() {
        //given
        String email = "hoanganh@gmail.com";
        String verifyCode = "1234";
        User user = new User();
        user.setVerifyCode(verifyCode);
        user.setEmail(email);

        when(userRepository.getUserByEmail(Mockito.any())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        RestResponse response = userService.activateUser(email, verifyCode);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(USER_ACTIVATE_SUCCESSFUL);
    }

    @Test
    void givenUserEmailAndVerifyCode_thenGetUserByEmailAndSaveUser_whenShouldReturnNotFoundAndCardActivateSuccessful() {
        //given
        String email = "hoanganh@gmail.com";
        String verifyCode = "1234";
        User user = new User();
        user.setVerifyCode("1111");
        user.setEmail(email);

        //when
        when(userRepository.getUserByEmail(Mockito.any())).thenReturn(Optional.of(user));

        //then
        Throwable exception = assertThrows(NotFoundException.class,
                () ->  userService.activateUser(email, verifyCode));
        assertEquals(CARD_ACTIVATE_FAILED, exception.getMessage());
    }

    @Test
    void updateUser_success() {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        Authentication authentication = Mockito.mock(Authentication.class);
        when(userRepository.getUserByEmail(Mockito.any())).thenReturn(Optional.of(Mockito.mock(User.class)));
        when(userRepository.save(Mockito.any())).thenReturn(Mockito.mock(User.class));

        RestResponse response = userService.updateUser(updateRequest, authentication);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(GET_USER_INFO_SUCCESS);
    }

    @Test
    void infoUser_success() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Card card = new Card();
        card.setCardNumber("123124");
        card.setDailyLimitAmount(123124.00);
        card.setMonthlyLimitAmount(123124.00);
        List<Card> list = new ArrayList<>();
        User user = new User();
        user.setEmail("hoanga@gmail.com");
        user.setCards(list);

        when(userRepository.getUserByEmail(Mockito.any())).thenReturn(Optional.of(user));
//        when(transactionRepository.dailyLimit(Mockito.any(),Mockito.any(),Mockito.anyInt(),Mockito.any(Date.class))).thenReturn(1.00);
//        when(transactionRepository.monthlyLimit(Mockito.any(),Mockito.any(),Mockito.anyInt() ,Mockito.anyInt(),Mockito.anyInt())).thenReturn(1.00);
        when(userRepository.save(Mockito.any())).thenReturn(user);

        RestResponse response = userService.infoUser(authentication);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(GET_USER_INFO_SUCCESS);
    }

    @Test
    void changePasswordByOldPassword_success() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        User user = new User();
        user.setEmail("hoanga@gmail.com");
        user.setPassword("$2[a$10$EulgXiN/bEwjJZc2IqRgoOyTcJWNZp0STtgY0fZv9XSIWigMHiBN2]");
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setPassword("123");
        changePasswordDTO.setNewPassword("123456789");
        changePasswordDTO.setConfirmNewPassword("123456789");

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(Mockito.mock(User.class)));
        when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(false);
        when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
        when(passwordEncoder.encode(Mockito.any())).thenReturn("$2[a$10$EulgXiN/bEwjJZc2IqRgoOyTcJWNZp0STtgY0fZv9XSIWigMHiBN2]");
        when(userRepository.save(Mockito.any())).thenReturn(user);

        RestResponse response = userService.changePasswordByOldPassword(authentication, changePasswordDTO);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(CHANGE_PASSWORD_SUCCESSFUL);
    }

    @Test
    void refreshToken() {
        //given
        String token = "123";
        String email = "hoanganh@gmail.com";
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setEmail("hoanganh@gmail.com");
        loginRequest.setPassword("123");
        User user = new User();
        Session session = new Session();

        //when
        when(request.getHeader(Mockito.any())).thenReturn("Bearer 123456");
        when(sessionRepository.findSessionByRefreshToken(Mockito.any())).thenReturn(Optional.of(session));
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
        RestResponse<LoginResponse> response = userService.refreshToken();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(GENERATE_TOKEN_AND_REFRESH_TOKEN_SUCCESS);
        Assertions.assertThat(response.getData()).isNotNull();

    }

    @Test
    void logout() {
        RestResponse response = userService.logout();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(USER_LOGOUT_SUCCESS);
    }

    @Test
    void findOneUser_success() {

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(Mockito.mock(User.class)));

        RestResponse response = userService.findOneUser("hoanganh@gmail.com");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(GET_USER_INFO_SUCCESS);
    }

    @Test
    void findAllUserFiltered_Success() {

        //given
        Page<User> cards = Mockito.mock(Page.class);
        FilterUserDTO filterUserDTO = new FilterUserDTO();
        filterUserDTO.setPageNumber(0);
        filterUserDTO.setPageSize(10);
        Pageable pageable = PageRequest.of(filterUserDTO.getPageNumber(), filterUserDTO.getPageSize());

        //when
        when(userRepository.findAllUserFiltered(filterUserDTO, pageable)).thenReturn(cards);

        //then
        RestResponse<?> response = userService.findAllUserFiltered(filterUserDTO);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(GET_PAGE_USER_SUCCESSFUL);
        Assertions.assertThat(response.getData()).isNotNull();
    }

    @Test
    void findAllUserFilteredWithSorting_Success() {

        //given
        Page<User> cards = Mockito.mock(Page.class);
        FilterUserDTO filterUserDTO = new FilterUserDTO();
        filterUserDTO.setPageNumber(0);
        filterUserDTO.setPageSize(10);
        filterUserDTO.setSortDir("10");
        filterUserDTO.setSortField("10");
        Sort sort = Sort.by(filterUserDTO.getSortField());
        sort = filterUserDTO.getSortDir().equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(filterUserDTO.getPageNumber(), filterUserDTO.getPageSize(), sort);

        //when
        when(userRepository.findAllUserFiltered(filterUserDTO, pageable)).thenReturn(cards);

        //then
        RestResponse<?> response = userService.findAllUserFiltered(filterUserDTO);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(GET_PAGE_USER_SUCCESSFUL);
        Assertions.assertThat(response.getData()).isNotNull();
    }

    @Test
    void userAdminCreate_success() {
    }

    @Test
    void userAdminUpdate_Success() {
        UserDTO userDTO = new UserDTO();
        Authentication authentication = Mockito.mock(Authentication.class);

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(Mockito.mock(User.class)));
        when(userRepository.save(Mockito.any())).thenReturn(Mockito.mock(User.class));

        RestResponse response = userService.userAdminUpdate("hoanganh@gmail.com", userDTO, authentication);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(UPDATE_SUCCESSFUL);

    }

    @Test
    void userAdminUpdate_Failure() {
        Authentication authentication = Mockito.mock(Authentication.class);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("hoanganh@gmail.com");
        userDTO.setPhone("1234");
        User user = new User();
        user.setEmail("hoanganh@gmail.com");
        user.setPhone("1234");

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        when(userRepository.findByPhone(Mockito.any())).thenReturn(Optional.of(user));

        Throwable exception = assertThrows(CompareException.class,
                () ->  userService.userAdminUpdate("hoanganh@gmail.com", userDTO, authentication));
        assertEquals(NEW_PHONE_NUMBER_ALREADY_EXISTS, exception.getMessage());
    }

    @Test
    void userAdminDisabled_Success() {
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(Mockito.mock(User.class)));
        when(userRepository.save(Mockito.any())).thenReturn(Mockito.mock(User.class));

        RestResponse response = userService.userAdminDisabled("hoanganh@gmail.com");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(USER_DISABLED_SUCCESS);
    }

    @Test
    void userAdminDecentralization_Success() {
        DecentralizationDTO dto =  new DecentralizationDTO();
        Authentication authentication = Mockito.mock(Authentication.class);

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(Mockito.mock(User.class)));
        when(userRepository.save(Mockito.any())).thenReturn(Mockito.mock(User.class));

        RestResponse response = userService.userAdminDecentralization(dto, authentication);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(USER_DECENTRALIZATION_SUCCESS);
    }

    @Test
    void findAllCardUser_Success() {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(Mockito.mock(User.class)));

        RestResponse response = userService.findAllCardUser(authentication);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(GET_USER_INFO_SUCCESS);
    }
}