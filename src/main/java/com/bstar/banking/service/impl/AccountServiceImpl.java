package com.bstar.banking.service.impl;

import com.bstar.banking.entity.Account;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.response.PinCodeResponse;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.AccountRepository;
import com.bstar.banking.service.AccountService;
import org.springframework.stereotype.Service;

import static com.bstar.banking.common.AccountString.*;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public RestResponse<PinCodeResponse> checkPinCode(PinCodeDTO pinCodeDTO) {
        Account account = accountRepository.findById(pinCodeDTO.getAccountNumber())
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND));
        if(account.getPinCode().equals(pinCodeDTO.getPinCode())){
            return new RestResponse<>(new PinCodeResponse("OK", ACCOUNT_PIN_CODE_MATCH));
        }else {
            return new RestResponse<>(new PinCodeResponse("INVALID_PIN_CODE", ACCOUNT_PIN_CODE_DOES_NOT_MATCH));
        }

    }
}
