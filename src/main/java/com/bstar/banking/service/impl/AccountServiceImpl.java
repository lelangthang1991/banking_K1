package com.bstar.banking.service.impl;

import com.bstar.banking.entity.Account;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.model.request.AccountDTO;
import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.response.PinCodeResponse;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.AccountRepository;
import com.bstar.banking.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.bstar.banking.common.AccountString.*;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public AccountServiceImpl(AccountRepository accountRepository, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public RestResponse<PinCodeResponse> checkPinCode(PinCodeDTO pinCodeDTO) {
        Account account = accountRepository.findById(pinCodeDTO.getAccountNumber())
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND));
        if (account.getPinCode().equals(pinCodeDTO.getPinCode())) {
            return new RestResponse<>(new PinCodeResponse("OK", ACCOUNT_PIN_CODE_MATCH));
        } else {
            return new RestResponse<>(new PinCodeResponse("INVALID_PIN_CODE", ACCOUNT_PIN_CODE_DOES_NOT_MATCH));
        }
    }
    @Override
    public RestResponse<ResponsePageAccount> findAccountByKeyword(String keyword, Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAccountByKeyword(keyword, pageable);
        List<AccountDTO> categoryDTOS = accountPage.getContent()
                        .parallelStream()
                        .map(account -> modelMapper.map(account, AccountDTO.class))
                        .collect(Collectors.toList());
        return new RestResponse<>(new ResponsePageAccount(accountPage.getNumber(),
                categoryDTOS.size(),
                accountPage.getTotalPages(),
                categoryDTOS));
    }
    @Override
    public RestResponse<ResponsePageAccount> findAccountByKeywordAndActivated(String keyword, boolean isActivated , Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAccountByKeywordAndActivated(keyword, isActivated ,pageable);
        List<AccountDTO> categoryDTOS = accountPage.getContent()
                .parallelStream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
        return new RestResponse<>(new ResponsePageAccount(accountPage.getNumber(),
                accountPage.getTotalElements(),
                accountPage.getTotalPages(),
                categoryDTOS));
    }


}
