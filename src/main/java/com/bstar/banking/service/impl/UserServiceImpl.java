package com.bstar.banking.service.impl;


import com.bstar.banking.common.RandomVerifycode;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.BusinessException;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.jwt.JwtUtil;
import com.bstar.banking.model.request.ForgotPasswordDTO;
import com.bstar.banking.model.request.LoginDTO;
import com.bstar.banking.model.request.SignupRequest;
import com.bstar.banking.model.request.UserUpdateRequest;
import com.bstar.banking.model.response.ForgotPasswordResponse;
import com.bstar.banking.model.response.LoginResponse;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.model.response.UserInfoResponse;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.security.UserDetailsServiceImpl;
import com.bstar.banking.service.AbstractCommonService;
import com.bstar.banking.service.MailerService;
import com.bstar.banking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
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

    private final RandomVerifycode verifyCode = new RandomVerifycode();

    @Autowired
    MailerService mailerService;

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
            return new RestResponse<>(new ForgotPasswordResponse("404", VERIFY_PASSWORD_DOES_NOT_MATCH));
        }
    }




    @Override
    public void addUserSignup(SignupRequest signupRequest, User user, String verifyCode) {
        //save user
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstname());
        user.setLastName(signupRequest.getLastname());
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
    }


    //feature/userinfo
    @Override
    public void updateUser(UserUpdateRequest updateRequest, User user) {

        user.setLastName(updateRequest.getLastname());
        user.setFirstName(updateRequest.getFirstname());
        user.setUpdate_date(new Date());
        user.setUpdate_person("hoanganh25022000@gmail.com");
        user.setPhone(updateRequest.getPhone());
        user.setAddress(updateRequest.getAddress());
        userRepository.save(user);

    }

    @Override
    public UserInfoResponse returninfo(User user) {
        UserInfoResponse userresponse = new UserInfoResponse();
        userresponse.setEmail(user.getEmail());
        userresponse.setFirstName(user.getFirstName());
        userresponse.setLastName(user.getLastName());
        userresponse.setDob(user.getDob());
        userresponse.setGender(user.getGender());
        userresponse.setAddress(user.getAddress());
        userresponse.setPhone(user.getPhone());
        userresponse.setCreate_date(user.getCreate_date());
        userresponse.setUpdate_date(user.getUpdate_date());
        return userresponse;
    }



    @Override
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest){

        //check email
        if (userRepository.existsById(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(EMAIl_WAS_REGISTERED);
        }

        //check phone

        if (userRepository.findByPhone(signupRequest.getPhone()) != null) {
            return ResponseEntity.badRequest().body(PHONE_WAS_REGISTERED);
        }
        //check password
        if (!signupRequest.getPassword().equals(signupRequest.getConfirm())) {
            return ResponseEntity.badRequest().body(PASSWORD_DOES_NOT_MATCH);
        }


        try {
            User user = new User();
            //Random verify code
            String verifycode = verifyCode.Random();
            //Save user
            this.addUserSignup(signupRequest, user, verifycode);
            // Send mail to user account
            mailerService.sendWelcome(user, verifycode);
            return ResponseEntity.ok(PLEASE_CHECK_YOUR_EMAIL);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(REGISTRATION_FAILED);

        }
    }

    @Override
    public ResponseEntity<?> activate(@PathVariable String email, @PathVariable String verify){
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        if (user.getVerifyCode().equals(verify)) {
            user.setIsActivated(true);
            userRepository.save(user);
            return ResponseEntity.ok(SUCCESSFUL_ACCOUNT_ACTIVATION);
        }
        return ResponseEntity.badRequest().body(ACCOUNT_ACTIVATION_FAILED);
    }

    @Override
    public ResponseEntity<?> update(@RequestBody UserUpdateRequest updateRequest, Authentication authentication){

        String username = authentication.getName();
        if (!userRepository.existsById(username)) {
            return ResponseEntity.badRequest().body(USER_NOT_FOUND);
        }

        //find user email who is specifying
        User user = userRepository.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));
        this.updateUser(updateRequest, user);
        //modify firstName; lastName;  address; phone; Date of birth; updatePerson ; updateDate;
        return ResponseEntity.ok(UPDATE_SUCCESSFUL);

    }

    @Override
    public ResponseEntity<?> info(Authentication authentication){
        if (!userRepository.existsById(authentication.getName())) {
            return ResponseEntity.badRequest().body(USER_NOT_FOUND);
        }
        User user = userRepository.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));
        return ResponseEntity.ok(this.returninfo(user));
//
    }

}
