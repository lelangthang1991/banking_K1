package com.bstar.banking.service;

import com.bstar.banking.model.request.ForgotPasswordDTO;
import com.bstar.banking.model.request.LoginDTO;
import com.bstar.banking.model.response.ForgotPasswordResponse;
import com.bstar.banking.model.response.LoginResponsePayload;
import com.bstar.banking.model.response.RestResponse;


public interface UserService {
    RestResponse<LoginResponsePayload> generateTokenAndRefreshToken(LoginDTO loginRequest) throws Exception;

    RestResponse<ForgotPasswordResponse> forgotPassWord(ForgotPasswordDTO forgotPasswordDTO);
}
