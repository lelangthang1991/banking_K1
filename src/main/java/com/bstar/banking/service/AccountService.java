package com.bstar.banking.service;

import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.response.CommonResponse;
import com.bstar.banking.model.response.PinCodeResponse;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface AccountService {
    RestResponse<PinCodeResponse> checkPinCode(PinCodeDTO pinCodeDTO, Authentication authentication);

    RestResponse<ResponsePageAccount> findAccountByKeyword(String keyword, Pageable pageable);

    RestResponse<ResponsePageAccount> findAccountByKeywordAndActivated(String keyword, boolean isActivated, Pageable pageable);

    RestResponse<CommonResponse> findAccountByEmail(String email);
    RestResponse<ResponsePageAccount> findPageAccount(Pageable pageable);

    RestResponse<CommonResponse> findAccountByAccountNumber(String accountNumber);

    RestResponse<CommonResponse> accountDisabled(String accountNumber);
}
