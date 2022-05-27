package com.bstar.banking.service;

import com.bstar.banking.entity.User;
import com.bstar.banking.model.request.ForgotPasswordDTO;
import com.bstar.banking.model.request.LoginDTO;
import com.bstar.banking.model.request.SignupRequest;
import com.bstar.banking.model.request.UserUpdateRequest;
import com.bstar.banking.model.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


public interface UserService {
    RestResponse<LoginResponse> generateTokenAndRefreshToken(LoginDTO loginRequest) throws Exception;

    RestResponse<ForgotPasswordResponse> forgotPassWord(ForgotPasswordDTO forgotPasswordDTO);


    void addUserSignup(SignupRequest signupRequest, User user, String verifyCode);

    void updateUser(UserUpdateRequest updateRequest, User user);

    UserInfoResponse returninfo(User user);

   RestResponse<SignUpResponse> signup(@Valid @RequestBody SignupRequest signupRequest);

    RestResponse<CommonResponse> activate(@PathVariable String email, @PathVariable String verify);

    RestResponse<UserInfoResponse> update(@RequestBody UserUpdateRequest updateRequest, Authentication authentication);

    RestResponse<UserInfoResponse> info(Authentication authentication);
}
