package com.bstar.banking.controller;

import com.bstar.banking.common.RandomVerifycode;
import com.bstar.banking.model.request.*;
import com.bstar.banking.model.response.*;
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


    @PostMapping("/signup-user")
    public ResponseEntity<RestResponse<CommonResponse>> signupUser(@Valid @RequestBody SignupRequest signupRequest) {
        RestResponse<CommonResponse> response = userService.signupUser(signupRequest);
        if (response.getData().getStatusCode().equals("200"))
            return ResponseEntity.ok(response);

        return ResponseEntity.badRequest().body(response);

    }

    @GetMapping("/activate-user/{email}/{verify}")
    public ResponseEntity<RestResponse<CommonResponse>> activateUser(@PathVariable String email, @PathVariable String verify) {
        RestResponse<CommonResponse> response = userService.activateUser(email, verify);
        if (response.getData().getStatusCode().equals("200"))
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);

    }

    @PostMapping("/update-user")
    public ResponseEntity<?> update(@Valid @RequestBody UserUpdateRequest updateRequest, Authentication authentication) {
        RestResponse<CommonResponse> response = userService.updateUser(updateRequest, authentication);
        if (response.getData().getStatusCode().equals("200"))
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/info-user")
    public ResponseEntity<RestResponse<CommonResponse>> infoUser(Authentication authentication) {

        RestResponse<CommonResponse> response = userService.infoUser(authentication);
        if (response.getData().getStatusCode().equals("200"))
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }
}
