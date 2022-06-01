package com.bstar.banking.service;

import com.bstar.banking.model.request.*;
import com.bstar.banking.model.response.LoginResponse;
import com.bstar.banking.model.response.RestResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;


public interface UserService {
    RestResponse<LoginResponse> generateTokenAndRefreshToken(LoginDTO loginRequest) throws Exception;

    RestResponse<?> forgotPassWord(ForgotPasswordDTO forgotPasswordDTO);

    RestResponse<?> signupUser(SignupRequest signupRequest);

    RestResponse<?> activateUser(@PathVariable String email, @PathVariable String verify);

    RestResponse<?> updateUser(UserUpdateRequest updateRequest, Authentication authentication);

    RestResponse<?> infoUser(Authentication authentication);

    RestResponse<?> changePasswordByOldPassword(Authentication authentication, ChangePasswordDTO changePasswordDTO) throws Exception;

    RestResponse<LoginResponse> refreshToken();

    RestResponse<?> logout();
}
