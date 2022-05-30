package com.bstar.banking.service.impl;


import com.bstar.banking.common.RandomVerifycode;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.BusinessException;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.jwt.JwtUtil;
import com.bstar.banking.model.request.*;
import com.bstar.banking.model.response.CommonResponse;
import com.bstar.banking.model.response.ForgotPasswordResponse;
import com.bstar.banking.model.response.LoginResponse;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.security.UserDetailsServiceImpl;
import com.bstar.banking.service.AbstractCommonService;
import com.bstar.banking.service.MailerService;
import com.bstar.banking.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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

    private final ModelMapper modelMapper;

    private final RandomVerifycode verifyCode = new RandomVerifycode();

    @Autowired
    MailerService mailerService;

    public UserServiceImpl(UserRepository userRepository,
                           JwtUtil jwtUtil,
                           UserDetailsServiceImpl userDetailsService,
                           JavaMailSender sender,
                           AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager1, ModelMapper modelMapper) {
        super(sender, authenticationManager);
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager1;
        this.modelMapper = modelMapper;
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
        LoginResponse data = new LoginResponse("200",
                GENERATE_TOKEN_AND_REFRESH_TOKEN_SUCCESS,
                token,
                tokenExpireMillis,
                refreshToken,
                refreshTokenExpireMillis);
        return new RestResponse<>(data);
    }

    @Override
    public RestResponse<ForgotPasswordResponse> forgotPassWord(ForgotPasswordDTO forgotPasswordDTO) {
        User user = userRepository.findById(forgotPasswordDTO.getEmail()).orElseThrow(() -> new NotFoundException("404", GET_USER_EMAIL_NOT_FOUND));
        if (user.getVerifyCode().equals(forgotPasswordDTO.getVerifyCode())) {
            user.setVerifyCode("");
            user.setPassword(passwordEncoder.encode(forgotPasswordDTO.getPassword()));
            userRepository.save(user);
            return new RestResponse<>(new ForgotPasswordResponse("200", CHANGE_PASSWORD_SUCCESS));
        } else {
            return new RestResponse<>(new ForgotPasswordResponse("401", VERIFY_PASSWORD_DOES_NOT_MATCH));
        }
    }


    @Override
    public RestResponse<CommonResponse> signupUser(SignupRequest signupRequest) {
        boolean isMailRegistered = userRepository.findById(signupRequest.getEmail()).isPresent();
        boolean isPhoneRegistered = userRepository.findByPhone(signupRequest.getPhone()).isPresent();
        if (isMailRegistered) {
            return new RestResponse<>(new CommonResponse("400", EMAIl_WAS_REGISTERED));
        } else if (isPhoneRegistered) {
            return new RestResponse<>(new CommonResponse("400", PHONE_WAS_REGISTERED));

        } else if (!signupRequest.getPassword().equals(signupRequest.getConfirm())) {
            return new RestResponse<>(new CommonResponse("400", PASSWORD_DOES_NOT_MATCH));
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
                return new RestResponse<>(new CommonResponse("200",
                        GET_USER_INFO_SUCCESS,
                        modelMapper.map(user, UserDTO.class)));
            } catch (Exception e) {
                return new RestResponse<>(new CommonResponse("400", REGISTRATION_FAILED));
            }
        }
    }

    @Override
    public RestResponse<CommonResponse> activateUser(@PathVariable String email, @PathVariable String verify) {
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        if (user.getVerifyCode().equals(verify)) {
            user.setRole(1);
            user.setIsActivated(true);
            userRepository.save(user);
            return new RestResponse<>(new CommonResponse("200", SUCCESSFUL_ACCOUNT_ACTIVATION));
        }
        return new RestResponse<>(new CommonResponse("400", ACCOUNT_ACTIVATION_FAILED));
    }

    @Override
    public RestResponse<CommonResponse> updateUser(UserUpdateRequest updateRequest, Authentication authentication) {
        User user = userRepository.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
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
        return new RestResponse<>(new CommonResponse("200",
                GET_USER_INFO_SUCCESS,
                modelMapper.map(user, UserDTO.class)));

    }

    @Override
    public RestResponse<CommonResponse> infoUser(Authentication authentication) {
        User user = userRepository.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        return new RestResponse<>(new CommonResponse("200",
                GET_USER_INFO_SUCCESS,
                modelMapper.map(user, UserDTO.class)));
    }

}
