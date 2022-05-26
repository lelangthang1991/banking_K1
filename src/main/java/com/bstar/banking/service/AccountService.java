package com.bstar.banking.service;

import com.bstar.banking.entity.Account;
import com.bstar.banking.model.request.AccountRequest;
import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.response.CommonResponse;
import com.bstar.banking.model.response.PinCodeResponse;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface AccountService {
    RestResponse<PinCodeResponse> checkPinCode(PinCodeDTO pinCodeDTO);

    RestResponse<ResponsePageAccount> findAccountByKeyword(String keyword, Pageable pageable);

    RestResponse<ResponsePageAccount> findAccountByKeywordAndActivated(String keyword, boolean isActivated, Pageable pageable);

    RestResponse<CommonResponse> findAccountByEmail(String email);
    RestResponse<ResponsePageAccount> findPageAccount(Pageable pageable);

    RestResponse<CommonResponse> findAccountByAccountNumber(String accountNumber);
    void saveBankAccount(Account account, AccountRequest accountRequest, String email);

    ResponseEntity<?> bankregister(@Valid @RequestBody AccountRequest bankrequest, Authentication authentication);
}
