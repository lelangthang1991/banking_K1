package com.bstar.banking.controller;

import com.bstar.banking.common.RandomVerifycode;
import com.bstar.banking.model.request.*;
import com.bstar.banking.model.response.ForgotPasswordResponse;
import com.bstar.banking.model.response.LoginResponse;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.service.MailerService;
import com.bstar.banking.service.UserService;
import com.bstar.banking.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;


@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final MailerService mailerService;

    @Autowired
    UserRepository userrepo;
    @Autowired
    UserServiceImpl userServiceImpl;

    private final BCryptPasswordEncoder passwordEncoder;
    private final RandomVerifycode verifyCode = new RandomVerifycode();

    @PostMapping("/login")
    public ResponseEntity<RestResponse<LoginResponse>> login(@Valid @RequestBody LoginDTO loginRequest) throws Exception {
        RestResponse<LoginResponse> response = userService.generateTokenAndRefreshToken(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-mail")
    public ResponseEntity<?> sendMail(@Valid @RequestBody EmailRequest mailer) throws MessagingException {
        RestResponse<ForgotPasswordResponse> response = mailerService.sendVerifyCode(mailer.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<RestResponse<ForgotPasswordResponse>> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotDTO) {
        RestResponse<ForgotPasswordResponse> response = userService.forgotPassWord(forgotDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken() {
        return ResponseEntity.ok("data");
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return userService.signup(signupRequest);


    }

    @GetMapping("/activate/{email}/{verify}")
    public ResponseEntity<?> activate(@PathVariable String email, @PathVariable String verify) {
        return userService.activate(email, verify);


    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserUpdateRequest updateRequest, Authentication authentication) {

        return userService.update(updateRequest, authentication);


    }


    //need user who is login in this page
    @GetMapping("/info")
    public ResponseEntity<?> info(Authentication authentication) {


        return userService.info(authentication);
    }
}
