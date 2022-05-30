package com.bstar.banking.controller;

import com.bstar.banking.model.request.*;
import com.bstar.banking.model.response.LoginResponse;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.MailerService;
import com.bstar.banking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

import static com.bstar.banking.common.StatusCodeString.OK;


@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final MailerService mailerService;
    @PostMapping("/login")
    public ResponseEntity<RestResponse<LoginResponse>> login(@Valid @RequestBody LoginDTO loginRequest) throws Exception {
        return ResponseEntity.ok(userService.generateTokenAndRefreshToken(loginRequest));
    }

    @PostMapping("/send-mail")
    public ResponseEntity<RestResponse<?>> sendMail(@RequestBody @Valid EmailRequest mailer) throws MessagingException {
        RestResponse<?> response = mailerService.sendVerifyCode(mailer.getEmail());
        if(response.getStatusCode().equals(OK)){
            return ResponseEntity.ok(response);
        }
        else{
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<RestResponse<?>> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotDTO) {
        RestResponse<?> response = userService.forgotPassWord(forgotDTO);
        if(response.getStatusCode().equals(OK)){
            return ResponseEntity.ok(response);
        }
        else{
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken() {
        return ResponseEntity.ok("data");
    }


    @PostMapping("/signup-user")
    public ResponseEntity<RestResponse<?>> signupUser(@Valid @RequestBody SignupRequest signupRequest) {
        RestResponse<?> response = userService.signupUser(signupRequest);
        if (response.getStatusCode().equals(OK)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/activate-user/{email}/{verify}")
    public ResponseEntity<RestResponse<?>> activateUser(@PathVariable String email, @PathVariable String verify) {
        RestResponse<?> response = userService.activateUser(email, verify);
        if (response.getStatusCode().equals(OK)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/update-user")
    public ResponseEntity<?> update(@Valid @RequestBody UserUpdateRequest updateRequest, Authentication authentication) {
        RestResponse<?> response = userService.updateUser(updateRequest, authentication);
        if (response.getStatusCode().equals(OK)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/info-user")
    public ResponseEntity<RestResponse<?>> infoUser(Authentication authentication) {
        return ResponseEntity.ok(userService.infoUser(authentication));
    }
}
