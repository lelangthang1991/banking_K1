package com.bstar.banking.service.impl;

import com.bstar.banking.common.RandomBankNumber;
import com.bstar.banking.entity.Account;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.model.request.AccountDTO;
import com.bstar.banking.model.request.ChangePinCodeDTO;
import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.request.RegisterBankAccountRq;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.AccountRepository;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.bstar.banking.common.AccountString.*;
import static com.bstar.banking.common.StatusCodeString.NOT_FOUND;
import static com.bstar.banking.common.StatusCodeString.OK;
import static com.bstar.banking.common.UserString.*;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RestResponse<?> checkPinCode(PinCodeDTO pinCodeDTO, Authentication authentication) {
        User user = userRepository.getUserByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException("404", "INVALID_EMAIL"));
        boolean isMatch = user.getAccounts().stream().anyMatch(acc -> {
            return acc.getPinCode().equals(pinCodeDTO.getPinCode()) && acc.getAccountNumber().equals(pinCodeDTO.getAccountNumber());
        });
        if (isMatch) {
            return new RestResponse<>(OK, ACCOUNT_PIN_CODE_MATCH);
        } else {
            return new RestResponse<>(NOT_FOUND, ACCOUNT_PIN_CODE_DOES_NOT_MATCH);
        }
    }

    @Override
    public RestResponse<ResponsePageAccount> findAccountByKeyword(String keyword, Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAccountByKeyword(keyword, pageable);
        List<AccountDTO> categoryDTOS = accountPage.getContent()
                .parallelStream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
        return new RestResponse<>(OK, GET_LIST_ACCOUNT_SUCCESS, new ResponsePageAccount(accountPage.getNumber(),
                categoryDTOS.size(),
                accountPage.getTotalPages(),
                categoryDTOS));
    }

    @Override
    public RestResponse<ResponsePageAccount> findAccountByKeywordAndActivated(String keyword, boolean isActivated, Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAccountByKeywordAndActivated(keyword, isActivated, pageable);
        List<AccountDTO> categoryDTOS = accountPage.getContent()
                .parallelStream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
        return new RestResponse<>(OK, GET_LIST_ACCOUNT_SUCCESS, new ResponsePageAccount(accountPage.getNumber(),
                accountPage.getTotalElements(),
                accountPage.getTotalPages(),
                categoryDTOS));
    }

    @Override
    public RestResponse<?> findAccountByEmail(String email) {
        List<AccountDTO> accountDTOS = accountRepository.findAccountByEmail(email)
                .stream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
        return new RestResponse<>(OK,
                GET_LIST_ACCOUNT_SUCCESS,
                accountDTOS);
    }

    @Override
    public RestResponse<ResponsePageAccount> findPageAccount(Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAll(pageable);
        List<AccountDTO> categoryDTOS = accountPage.getContent()
                .parallelStream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
        return new RestResponse<>(OK, GET_LIST_ACCOUNT_SUCCESS, new ResponsePageAccount(accountPage.getNumber(),
                accountPage.getTotalElements(),
                accountPage.getTotalPages(),
                categoryDTOS));
    }

    @Override
    public RestResponse<?> findAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND, ACCOUNT_NUMBER_NOT_FOUND));
        return new RestResponse<>(OK,
                GET_ACCOUNT_SUCCESS,
                modelMapper.map(account, AccountDTO.class));
    }

    @Override
    public RestResponse<?> accountUpdate(AccountDTO accountDTO, Authentication authentication) {
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        Account account = accountRepository.findById(accountDTO.getAccountNumber())
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NUMBER_NOT_FOUND));
        account.setBalance(accountDTO.getBalance());
        account.setPinCode(accountDTO.getPinCode());
        account.setAccountType(accountDTO.getAccountType());
        account.setIsActivated(accountDTO.getIsActivated());
        account.setUpdatePerson(user.getEmail());
        account.setUpdateDate(new Date());
        Account accountSave = accountRepository.save(account);
        return new RestResponse<>(OK, GET_LIST_ACCOUNT_SUCCESS, modelMapper.map(accountSave, AccountDTO.class));
    }

    @Override
    public RestResponse<?> accountDisabled(String accountNumber) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND, ACCOUNT_NUMBER_NOT_FOUND));
        account.setIsActivated(false);
        accountRepository.save(account);
        return new RestResponse<>(OK, ACCOUNT_DISABLED_SUCCESS);
    }

    public RestResponse<?> bankRegister(@Valid @RequestBody RegisterBankAccountRq registerBankAccountRq,
                                        Authentication authentication) {
        if (!registerBankAccountRq.getPinCode().equals(registerBankAccountRq.getConfirmPinCode())) {
            return new RestResponse<>(NOT_FOUND, PINCODE_DOES_NOT_MATCH);
        }
        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        Account account = new Account();
        RandomBankNumber randomBankNumber = new RandomBankNumber();
        account.setAccountType(registerBankAccountRq.getAccountType());
        account.setAccountNumber(randomBankNumber.randomBankNumber());
        account.setBalance((double) 0);
        account.setPinCode(registerBankAccountRq.getPinCode());
        account.setIsActivated(true);
        account.setCreateDate(new Date());
        account.setCreatePerson(email);
        account.setUpdateDate(new Date());
        account.setUpdatePerson(email);
        account.setUser(user);
        accountRepository.save(account);
        return new RestResponse<>(OK,
                ACCOUNT_REGISTRATION_SUCCESSFUL,
                modelMapper.map(account, AccountDTO.class));

    }

    @Override
    public RestResponse<?> changePinCode(ChangePinCodeDTO changePinCodeDTO, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        Account account = user.getAccounts().stream()
                .filter(us -> us.getAccountNumber().equals(changePinCodeDTO.getAccountNumber()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ACCOUNT_PIN_CODE_DOES_NOT_MATCH));
        account.setPinCode(changePinCodeDTO.getNewPinCode());
        accountRepository.save(account);
        return new RestResponse<>(OK,
                ACCOUNT_CHANGE_PIN_CODE_SUCCESSFUL,
                modelMapper.map(account, AccountDTO.class));
    }

}
