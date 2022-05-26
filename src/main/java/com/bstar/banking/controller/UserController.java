package com.bstar.banking.controller;

import com.bstar.banking.common.RandomVerifycode;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.BusinessException;
import com.bstar.banking.exception.NotFoundException;
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

import static com.bstar.banking.common.UserString.*;


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
        userService.signup(signupRequest);
        return   userService.signup(signupRequest);

//        //check email
//        if (userrepo.existsById(signupRequest.getEmail())) {
//            return ResponseEntity.badRequest().body(EMAIl_WAS_REGISTERED);
//        }
//
//        //check phone
//
//        if (userrepo.findByPhone(signupRequest.getPhone()) != null) {
//            return ResponseEntity.badRequest().body(PHONE_WAS_REGISTERED);
//        }
//        //check password
//        if (!signupRequest.getPassword().equals(signupRequest.getConfirm())) {
//            return ResponseEntity.badRequest().body(PASSWORD_DOES_NOT_MATCH);
//        }
//
//
//        try {
//            User user = new User();
//            //Random verify code
//            String verifycode = verifyCode.Random();
//            //Save user
//            userService.addUserSignup(signupRequest, user, verifycode);
//            // Send mail to user account
//            mailerService.sendWelcome(user, verifycode);
//            return ResponseEntity.ok(PLEASE_CHECK_YOUR_EMAIL);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(REGISTRATION_FAILED);
//
//        }

    }

    @GetMapping("/activate/{email}/{verify}")
    public ResponseEntity<?> activate(@PathVariable String email, @PathVariable String verify) {
        userService.activate(email,verify);
        return  userService.activate(email,verify);
//        User user = userrepo.getUserByEmail(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
//        if (user.getVerifyCode().equals(verify)) {
//            user.setIsActivated(true);
//            userrepo.save(user);
//            return ResponseEntity.ok(SUCCESSFUL_ACCOUNT_ACTIVATION);
//        }
//        return ResponseEntity.badRequest().body(ACCOUNT_ACTIVATION_FAILED);

    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserUpdateRequest updateRequest, Authentication authentication) {

        return   userService.update(updateRequest,authentication);

//        String username = authentication.getName();
//        if (!userrepo.existsById(username)) {
//            return ResponseEntity.badRequest().body(USER_NOT_FOUND);
//        }
//
//        //find user email who is specifying
//        User user = userrepo.getUserByEmail(authentication.getName())
//                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));
//        userService.updateUser(updateRequest, user);
//        //modify firstName; lastName;  address; phone; Date of birth; updatePerson ; updateDate;
//        return ResponseEntity.ok(UPDATE_SUCCESSFUL);
    }


    //need user who is login in this page
    @GetMapping("/info")
    public ResponseEntity<?> info(Authentication authentication) {


        return  userService.info(authentication);
    }
}
