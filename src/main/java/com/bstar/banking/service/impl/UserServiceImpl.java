package com.bstar.banking.service.impl;


import com.bstar.banking.common.RandomVerifycode;
import com.bstar.banking.entity.Session;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.BusinessException;
import com.bstar.banking.exception.CompareException;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.jwt.JwtUtil;
import com.bstar.banking.model.request.*;
import com.bstar.banking.model.response.LoginResponse;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.SessionRepository;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.security.UserDetailsServiceImpl;
import com.bstar.banking.service.AbstractCommonService;
import com.bstar.banking.service.MailerService;
import com.bstar.banking.service.UserService;
import com.bstar.banking.utils.DeviceType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;

import static com.bstar.banking.common.JwtString.GENERATE_TOKEN_AND_REFRESH_TOKEN_SUCCESS;
import static com.bstar.banking.common.JwtString.REFRESH_TOKEN_NOT_FOUND;
import static com.bstar.banking.common.StatusCodeString.*;
import static com.bstar.banking.common.UserString.*;
import static java.util.Objects.nonNull;


@Transactional
@Service
public class UserServiceImpl extends AbstractCommonService implements UserService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final ModelMapper modelMapper;
    private final RandomVerifycode verifyCode = new RandomVerifycode();
    private final DeviceType deviceType;
    private final HttpServletRequest request;
    @Autowired
    MailerService mailerService;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, JavaMailSender sender, AuthenticationManager authenticationManager, SessionRepository sessionRepository, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper, DeviceType deviceType, HttpServletRequest request) {
        super(sender, authenticationManager);
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.deviceType = deviceType;
        this.request = request;
    }

    @Override
    public RestResponse<LoginResponse> generateTokenAndRefreshToken(LoginDTO loginRequest) throws Exception {
        String password = loginRequest.getPassword();
        String email = loginRequest.getEmail();
        authenticate(email, password); // Call LDAP/Validate manual
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtUtil.generateToken(userDetails);
        Date tokenExpire = jwtUtil.getExpirationDateFromToken(token);
        long tokenExpireMillis = tokenExpire.getTime();
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        Date refreshTokenExpire = jwtUtil.getExpirationDateFromToken(refreshToken);
        long refreshTokenExpireMillis = refreshTokenExpire.getTime();
        Session session = new Session();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        String clientIp;
        String device_type = request.getHeader("user-agent");
        String clientXForwardedForIp = request.getHeader("x-forwarded-for");
        if (nonNull(clientXForwardedForIp)) {
            clientIp = deviceType.parseXForwardedHeader(clientXForwardedForIp);
        } else {
            clientIp = request.getRemoteAddr();
        }
        session.setRefreshToken(refreshToken);
        session.setExpired(refreshTokenExpire);
        session.setCreateDate(new Date());
        session.setDeviceType(device_type);
        session.setIpAddress(clientIp);
        session.setUser(user);
        sessionRepository.save(session);
        LoginResponse data = new LoginResponse(token, tokenExpireMillis, refreshToken, refreshTokenExpireMillis);
        return new RestResponse<>(OK, GENERATE_TOKEN_AND_REFRESH_TOKEN_SUCCESS, data);
    }


    @Override
    public RestResponse<?> forgotPassWord(ForgotPasswordDTO forgotPasswordDTO) {
        User user = userRepository.findById(forgotPasswordDTO.getEmail()).orElseThrow(() -> new NotFoundException("404", GET_USER_EMAIL_NOT_FOUND));
        if (user.getVerifyCode().equals(forgotPasswordDTO.getVerifyCode())) {
            user.setVerifyCode("");
            user.setPassword(passwordEncoder.encode(forgotPasswordDTO.getNewPassword()));
            userRepository.save(user);
            return new RestResponse<>(OK, CHANGE_PASSWORD_SUCCESS);
        } else {
            return new RestResponse<>(UNAUTHORIZED, VERIFY_PASSWORD_DOES_NOT_MATCH);
        }
    }

    @Override
    public RestResponse<?> signupUser(SignupRequest signupRequest) {
        boolean isMailRegistered = userRepository.findById(signupRequest.getEmail()).isPresent();
        boolean isPhoneRegistered = userRepository.findByPhone(signupRequest.getPhone()).isPresent();
        if (isMailRegistered) {
            return new RestResponse<>(BAD_REQUEST, EMAIl_WAS_REGISTERED);
        } else if (isPhoneRegistered) {
            return new RestResponse<>(BAD_REQUEST, PHONE_WAS_REGISTERED);

        } else if (!signupRequest.getPassword().equals(signupRequest.getConfirm())) {
            return new RestResponse<>(BAD_REQUEST, PASSWORD_DOES_NOT_MATCH);
        } else {
            try {
                User user = new User();
                String verifyCode = this.verifyCode.Random();
                user.setEmail(signupRequest.getEmail());
                user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
                user.setFirstName(signupRequest.getFirstName());
                user.setLastName(signupRequest.getLastName());
                user.setDob(signupRequest.getDob());
                user.setGender(signupRequest.getGender());
                user.setAddress(signupRequest.getAddress());
                user.setPhone(signupRequest.getPhone());
                user.setIsActivated(false);
                user.setCreate_date(new Date());
                user.setUpdate_date(new Date());
                user.setCreate_person(signupRequest.getEmail());
                user.setUpdate_person(signupRequest.getEmail());
                user.setVerifyCode(verifyCode);
                userRepository.save(user);
                mailerService.sendWelcome(user, verifyCode);
                return new RestResponse<>(OK, GET_USER_INFO_SUCCESS, modelMapper.map(user, UserDTO.class));
            } catch (Exception e) {
                return new RestResponse<>(BAD_REQUEST, REGISTRATION_FAILED);
            }
        }
    }

    @Override
    public RestResponse<?> activateUser(@PathVariable String email, @PathVariable String verify) {
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        if (user.getVerifyCode().equals(verify)) {
            user.setRole(1);
            user.setIsActivated(true);
            userRepository.save(user);
            return new RestResponse<>(OK, SUCCESSFUL_ACCOUNT_ACTIVATION);
        }
        return new RestResponse<>(BAD_REQUEST, ACCOUNT_ACTIVATION_FAILED);
    }

    @Override
    public RestResponse<?> updateUser(UserUpdateRequest updateRequest, Authentication authentication) {
        User user = userRepository.getUserByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        user.setEmail(user.getEmail());
        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(user.getLastName());
        user.setDob(updateRequest.getDob());
        user.setGender(updateRequest.getGender());
        user.setAddress(updateRequest.getAddress());
        user.setPhone(updateRequest.getPhone());
        user.setUpdate_date(new Date());
        user.setUpdate_person(user.getEmail());
        userRepository.save(user);
        return new RestResponse<>(OK, GET_USER_INFO_SUCCESS, modelMapper.map(user, UserDTO.class));
    }

    @Override
    public RestResponse<?> infoUser(Authentication authentication) {
        User user = userRepository.getUserByEmail(authentication.getName()).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        return new RestResponse<>(OK, GET_USER_INFO_SUCCESS, modelMapper.map(user, UserDTO.class));
    }

    @Override
    public RestResponse<?> changePasswordByOldPassword(Authentication authentication, ChangePasswordDTO changePasswordDTO) throws Exception {
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword())) {
            throw new CompareException(CONFIRM_PASSWORD_DOES_NOT_MATCH);
        }
        User user = userRepository.findById(authentication.getName()).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        if (user.getPassword().equals(changePasswordDTO.getPassword())) {
            throw new NotFoundException(PASSWORD_DOES_NOT_MATCH);
        }
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        return new RestResponse<>(OK, CHANGE_PASSWORD_SUCCESS);
    }

    @Override
    public RestResponse<LoginResponse> refreshToken() {
        final String requestTokenHeader = request.getHeader("Authorization");
        String email = "";
        String jwtToken = "";
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            email = jwtUtil.getEmailFromToken(jwtToken);
        }
        Session session = sessionRepository.findSessionByRefreshToken(jwtToken)
                .orElseThrow(() -> new NotFoundException(REFRESH_TOKEN_NOT_FOUND));
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtUtil.generateToken(userDetails);
        Date tokenExpire = jwtUtil.getExpirationDateFromToken(token);
        long tokenExpireMillis = tokenExpire.getTime();
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        Date refreshTokenExpire = jwtUtil.getExpirationDateFromToken(refreshToken);
        long refreshTokenExpireMillis = refreshTokenExpire.getTime();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        String clientIp = "";
        String device_type = request.getHeader("user-agent");
        String clientXForwardedForIp = request.getHeader("x-forwarded-for");
        if (nonNull(clientXForwardedForIp)) {
            clientIp = deviceType.parseXForwardedHeader(clientXForwardedForIp);
        } else {
            clientIp = request.getRemoteAddr();
        }
        session.setRefreshToken(refreshToken);
        session.setExpired(refreshTokenExpire);
        session.setCreateDate(new Date());
        session.setDeviceType(device_type);
        session.setIpAddress(clientIp);
        session.setUser(user);
        sessionRepository.save(session);
        LoginResponse data = new LoginResponse(token, tokenExpireMillis, refreshToken, refreshTokenExpireMillis);
        return new RestResponse<>(OK, GENERATE_TOKEN_AND_REFRESH_TOKEN_SUCCESS, data);
    }

    @Override
    public RestResponse<?> logout() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(null, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        return new RestResponse<>(OK, USER_LOGOUT_SUCCESS);
    }
}
