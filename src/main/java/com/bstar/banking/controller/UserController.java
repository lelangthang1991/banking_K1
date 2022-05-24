package com.bstar.banking.controller;

import com.bstar.banking.model.request.EmailRequest;
import com.bstar.banking.model.request.ForgotPasswordDTO;
import com.bstar.banking.model.request.LoginDTO;
import com.bstar.banking.model.response.CommonResponse;
import com.bstar.banking.model.response.ForgotPasswordResponse;
import com.bstar.banking.model.response.LoginResponsePayload;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.MailerService;
import com.bstar.banking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;


@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final MailerService mailerService;

    @PostMapping("/login")
    public ResponseEntity<RestResponse<LoginResponsePayload>> login(@Valid @RequestBody LoginDTO loginRequest) throws Exception {
        RestResponse<LoginResponsePayload> response = userService.generateTokenAndRefreshToken(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-mail")
    public ResponseEntity<?> sendMail(@Valid @RequestBody EmailRequest mailer) throws MessagingException {
        CommonResponse<ForgotPasswordResponse> response = mailerService.sendVerifyCode(mailer.getEmail());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<RestResponse<ForgotPasswordResponse>> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotDTO) {
        RestResponse<ForgotPasswordResponse> response = userService.forgotPassWord(forgotDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken() {
        return ResponseEntity.ok("data");
    }
}
