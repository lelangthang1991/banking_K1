package com.bstar.banking.service.impl;


import com.bstar.banking.entity.User;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.jwt.JwtUtil;
import com.bstar.banking.model.request.ForgotPasswordDTO;
import com.bstar.banking.model.request.LoginDTO;
import com.bstar.banking.model.response.ForgotPasswordResponse;
import com.bstar.banking.model.response.LoginResponse;
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
import static com.bstar.banking.common.UserString.*;


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
    public RestResponse<LoginResponse> generateTokenAndRefreshToken(LoginDTO loginRequest) throws Exception {
        String password = loginRequest.getPassword();
        String email = loginRequest.getEmail();
        authenticate(email, password);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtUtil.generateToken(userDetails);
        Date tokenExpire = jwtUtil.getExpirationDateFromToken(token);
        long tokenExpireMillis = tokenExpire.getTime();
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        Date refreshTokenExpire = jwtUtil.getExpirationDateFromToken(refreshToken);
        long refreshTokenExpireMillis = refreshTokenExpire.getTime();
        LoginResponse data = new LoginResponse("OK",
                GENERATE_TOKEN_AND_REFRESH_TOKEN_SUCCESS,
                token,
                tokenExpireMillis,
                refreshToken,
                refreshTokenExpireMillis);
        return new RestResponse<>(data);
    }

    @Override
    public RestResponse<ForgotPasswordResponse> forgotPassWord(ForgotPasswordDTO forgotPasswordDTO) {
        User user = userRepository.findById(forgotPasswordDTO.getEmail()).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        if (user.getVerifyCode().equals(forgotPasswordDTO.getVerifyCode())) {
            user.setVerifyCode("");
            user.setPassword(passwordEncoder.encode(forgotPasswordDTO.getPassword()));
            userRepository.save(user);
            return new RestResponse<>(new ForgotPasswordResponse("OK", CHANGE_PASSWORD_SUCCESS));
        } else {
            return new RestResponse<>(new ForgotPasswordResponse("INVALID_VERIFY_PASSWORD", VERIFY_PASSWORD_DOES_NOT_MATCH));
        }
    }
}
