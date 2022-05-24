package com.bstar.banking.service.impl;


import com.bstar.banking.entity.User;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.jwt.JwtUtil;
import com.bstar.banking.model.request.ForgotPasswordDTO;
import com.bstar.banking.model.request.LoginDTO;
import com.bstar.banking.model.response.ForgotPasswordResponse;
import com.bstar.banking.model.response.HeaderResponse;
import com.bstar.banking.model.response.LoginResponsePayload;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.security.UserDetailsServiceImpl;
import com.bstar.banking.service.AbstractCommonService;
import com.bstar.banking.service.UserService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.bstar.banking.common.JwtString.GENERATE_TOKEN_AND_REFRESH_TOKEN_SUCCESS;
import static com.bstar.banking.common.UserString.CHANGE_PASSWORD_SUCCESS;
import static com.bstar.banking.common.UserString.GET_USER_EMAIL_NOT_FOUND;


@Service
public class UserServiceImpl extends AbstractCommonService implements UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository,
                           JwtUtil jwtUtil,
                           UserDetailsServiceImpl userDetailsService,
                           JavaMailSender sender,
                           AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager1) {
        super(sender, authenticationManager);
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager1;
    }

    @Override
    public RestResponse<LoginResponsePayload> generateTokenAndRefreshToken(LoginDTO loginRequest) throws Exception {
        String password = loginRequest.getPassword();
        String email = loginRequest.getEmail();
        authenticate(email, password);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtUtil.generateToken(userDetails);
        Date tokenExpire = jwtUtil.getExpirationDateFromToken(token);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        Date refreshTokenExpire = jwtUtil.getExpirationDateFromToken(refreshToken);
        HeaderResponse header = new HeaderResponse();
        LoginResponsePayload body = new LoginResponsePayload("200",
                GENERATE_TOKEN_AND_REFRESH_TOKEN_SUCCESS,
                token,
                tokenExpire,
                refreshToken,
                refreshTokenExpire);
        return new RestResponse<>(header, body);
    }

    @Override
    public RestResponse<ForgotPasswordResponse> forgotPassWord(ForgotPasswordDTO forgotPasswordDTO) {
        User user = userRepository.getUserByEmail(forgotPasswordDTO.getEmail()).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        if (user.getVerifyCode().equals(forgotPasswordDTO.getVerifyCode())) {
            user.setVerifyCode("");
            user.setPassword(passwordEncoder.encode(forgotPasswordDTO.getPassword()));
            userRepository.save(user);
        }
        RestResponse<ForgotPasswordResponse> response =
                new RestResponse<>(new HeaderResponse(), new ForgotPasswordResponse("200", CHANGE_PASSWORD_SUCCESS));
        return response;
    }


}