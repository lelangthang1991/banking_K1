package com.bstar.banking.service;

import com.bstar.banking.model.request.AccountDTO;
import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.request.RegisterBankAccountRq;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface AccountService {
    RestResponse<?> checkPinCode(PinCodeDTO pinCodeDTO, Authentication authentication);

    RestResponse<ResponsePageAccount> findAccountByKeyword(String keyword, Pageable pageable);

    RestResponse<ResponsePageAccount> findAccountByKeywordAndActivated(String keyword, boolean isActivated, Pageable pageable);

    RestResponse<?> findAccountByEmail(String email);
    RestResponse<ResponsePageAccount> findPageAccount(Pageable pageable);

    RestResponse<?> findAccountByAccountNumber(String accountNumber);
    RestResponse<?> accountUpdate(AccountDTO accountDTO, Authentication authentication);
    RestResponse<?> accountDisabled(String accountNumber);

    RestResponse<?> bankRegister(RegisterBankAccountRq bankRequest, Authentication authentication);
}
