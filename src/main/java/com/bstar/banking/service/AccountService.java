package com.bstar.banking.service;

import com.bstar.banking.model.request.*;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import org.springframework.security.core.Authentication;

public interface AccountService {
    RestResponse<?> checkPinCode(PinCodeDTO pinCodeDTO, Authentication authentication);

    RestResponse<ResponsePageAccount> findAccountByKeyword(PagingRequest request);

    RestResponse<ResponsePageAccount> findAccountByKeywordAndActivated(PagingRequest request);

    RestResponse<?> findAccountByEmail(String email);

    RestResponse<ResponsePageAccount> findPageAccount(PagingRequest request);

    RestResponse<?> findAccountByAccountNumber(String accountNumber);

    RestResponse<?> accountUpdate(AccountDTO accountDTO, Authentication authentication);

    RestResponse<?> accountDisabled(String accountNumber);

    RestResponse<?> bankRegister(RegisterBankAccountRq bankRequest, Authentication authentication);

    RestResponse<?> adminBankRegister(AdminRegisterDTO registerDTO, Authentication authentication);


    RestResponse<?> changePinCode(ChangePinCodeDTO changePinCodeDTO, Authentication authentication);

    RestResponse<?> findAllAccountFiltered(FilterAccountDTO filterAccountDTO);
    RestResponse<?> activatedAccount(ActivateAccountDTO accountDTOs);
}
