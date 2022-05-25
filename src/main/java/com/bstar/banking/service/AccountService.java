package com.bstar.banking.service;

import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.response.PinCodeResponse;
import com.bstar.banking.model.response.RestResponse;

public interface AccountService {
    RestResponse<PinCodeResponse> checkPinCode(PinCodeDTO pinCodeDTO);
}
