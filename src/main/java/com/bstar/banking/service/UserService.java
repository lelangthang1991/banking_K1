package com.bstar.banking.service;

import com.bstar.banking.entity.User;
import com.bstar.banking.model.request.ForgotPasswordDTO;
import com.bstar.banking.model.request.LoginDTO;
import com.bstar.banking.model.request.SignupRequest;
import com.bstar.banking.model.request.UserUpdateRequest;
import com.bstar.banking.model.response.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


public interface UserService {
    RestResponse<LoginResponse> generateTokenAndRefreshToken(LoginDTO loginRequest) throws Exception;

    RestResponse<ForgotPasswordResponse> forgotPassWord(ForgotPasswordDTO forgotPasswordDTO);



    RestResponse<CommonResponse> signupUser(SignupRequest signupRequest);

    RestResponse<CommonResponse> activateUser(@PathVariable String email, @PathVariable String verify);

    RestResponse<CommonResponse> updateUser(UserUpdateRequest updateRequest, Authentication authentication);

    RestResponse<CommonResponse> infoUser(Authentication authentication);
}
