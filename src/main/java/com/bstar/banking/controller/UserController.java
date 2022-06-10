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
        return ResponseEntity.ok(mailerService.sendVerifyCode(mailer.getEmail()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<RestResponse<?>> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotDTO) {
        return ResponseEntity.ok(userService.forgotPassWord(forgotDTO));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken() {
        return ResponseEntity.ok(userService.refreshToken());
    }

    @PostMapping("/signup-user")
    public ResponseEntity<RestResponse<?>> signupUser(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(userService.signupUser(signupRequest));
    }

    @GetMapping("/activate-user/{email}/{verify}")
    public ResponseEntity<?> activateUser(@PathVariable String email, @PathVariable String verify) {
        return ResponseEntity.ok(userService.activateUser(email, verify));
    }

    @PostMapping("/update-user")
    public ResponseEntity<?> update(@Valid @RequestBody UserUpdateRequest updateRequest, Authentication authentication) {
        return ResponseEntity.ok(userService.updateUser(updateRequest, authentication));
    }

    @GetMapping("/info-user")
    public ResponseEntity<?> infoUser(Authentication authentication) {
        return ResponseEntity.ok(userService.infoUser(authentication));
    }

    @PostMapping("/change-password")
    public ResponseEntity<RestResponse<?>> changePasswordByOldPassword(Authentication authentication,
                                                                       @Valid @RequestBody ChangePasswordDTO changePasswordDTO) throws Exception {
        return ResponseEntity.ok(userService.changePasswordByOldPassword(authentication, changePasswordDTO));
    }

    @GetMapping("/logout")
    public ResponseEntity<RestResponse<?>> logout() {
        return ResponseEntity.ok(userService.logout());
    }

    @GetMapping("/get-card-user")
    public ResponseEntity<RestResponse<?>> findAllCardUser(Authentication authentication) {
        return ResponseEntity.ok(userService.findAllCardUser(authentication));
    }

}
