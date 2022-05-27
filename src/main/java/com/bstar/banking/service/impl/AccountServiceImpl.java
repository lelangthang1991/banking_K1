package com.bstar.banking.service.impl;

import com.bstar.banking.common.RandomBankNumber;
import com.bstar.banking.entity.Account;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.model.request.AccountDTO;
import com.bstar.banking.model.request.AccountRequest;
import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.response.*;
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
import static com.bstar.banking.common.UserString.PINCODE_DOES_NOT_MATCH;
import static com.bstar.banking.common.UserString.USER_NOT_FOUND;

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
    public RestResponse<PinCodeResponse> checkPinCode(PinCodeDTO pinCodeDTO, Authentication authentication) {
        User user = userRepository.getUserByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException("404", "INVALID_EMAIL"));
        boolean isMatch = user.getAccounts().stream().anyMatch(acc -> {
            return acc.getPinCode().equals(pinCodeDTO.getPinCode()) && acc.getAccountNumber().equals(pinCodeDTO.getAccountNumber());
        });
        if (isMatch) {
            return new RestResponse<>(new PinCodeResponse("200", ACCOUNT_PIN_CODE_MATCH));
        } else {
            return new RestResponse<>(new PinCodeResponse("404", ACCOUNT_PIN_CODE_DOES_NOT_MATCH));
        }
    }

    @Override
    public RestResponse<ResponsePageAccount> findAccountByKeyword(String keyword, Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAccountByKeyword(keyword, pageable);
        List<AccountDTO> categoryDTOS = accountPage.getContent().parallelStream().map(account -> modelMapper.map(account, AccountDTO.class)).collect(Collectors.toList());
        return new RestResponse<>(new ResponsePageAccount(accountPage.getNumber(), categoryDTOS.size(), accountPage.getTotalPages(), categoryDTOS));
    }

    @Override
    public RestResponse<ResponsePageAccount> findAccountByKeywordAndActivated(String keyword, boolean isActivated, Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAccountByKeywordAndActivated(keyword, isActivated, pageable);
        List<AccountDTO> categoryDTOS = accountPage.getContent().parallelStream().map(account -> modelMapper.map(account, AccountDTO.class)).collect(Collectors.toList());
        return new RestResponse<>(new ResponsePageAccount(accountPage.getNumber(), accountPage.getTotalElements(), accountPage.getTotalPages(), categoryDTOS));
    }

    @Override
    public RestResponse<CommonResponse> findAccountByEmail(String email) {
        List<AccountDTO> accountDTOS = accountRepository.findAccountByEmail(email).stream().map(account -> modelMapper.map(account, AccountDTO.class)).collect(Collectors.toList());
        return new RestResponse<>(new CommonResponse("200", "Get account list success", accountDTOS));
    }

    @Override
    public RestResponse<ResponsePageAccount> findPageAccount(Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAll(pageable);
        List<AccountDTO> categoryDTOS = accountPage.getContent().parallelStream().map(account -> modelMapper.map(account, AccountDTO.class)).collect(Collectors.toList());
        return new RestResponse<>(new ResponsePageAccount(accountPage.getNumber(), accountPage.getTotalElements(), accountPage.getTotalPages(), categoryDTOS));
    }

    @Override
    public RestResponse<CommonResponse> findAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findById(accountNumber).orElseThrow(() -> new NotFoundException("404", ACCOUNT_NUMBER_NOT_FOUND));
        return new RestResponse<>(new CommonResponse("200", "Get account success", modelMapper.map(account, AccountDTO.class)));
    }

    @Override
    public RestResponse<CommonResponse> accountDisabled(String accountNumber) {
        Account account = accountRepository.findById(accountNumber).orElseThrow(() -> new NotFoundException("404", ACCOUNT_NUMBER_NOT_FOUND));
        account.setIsActivated(false);
        accountRepository.save(account);
        return new RestResponse<>(new CommonResponse("200", "Account disabled success"));
    }


    @Override
    public void saveBankAccount(Account account, AccountRequest accountRequest, String email) {
        RandomBankNumber randomBankNumber = new RandomBankNumber();
        account.setAccountType(accountRequest.getAccountType());
        account.setAccountNumber(randomBankNumber.randomBankNumber());
        account.setBalance((double) 0);
        account.setPinCode(accountRequest.getPinCode());
        account.setIsActivated(true);
        account.setCreateDate(new Date());
        account.setCreatePerson(email);
        account.setUpdateDate(new Date());
        account.setUpdatePerson(email);
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        account.setUser(user);
        accountRepository.save(account);
    }


    public RestResponse<AccountResponse> bankregister(@Valid @RequestBody AccountRequest bankrequest, Authentication authentication) {
        String email = authentication.getName();
        if (!userRepository.existsById(authentication.getName())) {
            return new RestResponse<>(new AccountResponse("404", USER_NOT_FOUND));

        }

        if (!bankrequest.getPinCode().equals(bankrequest.getConfirmPinCode())) {
            return new RestResponse<>(new AccountResponse("404", PINCODE_DOES_NOT_MATCH));

        }
        Account account = new Account();

        this.saveBankAccount(account, bankrequest, email);
        return new RestResponse<>(new AccountResponse("200", ACCOUNT_REGISTRATION_SUCCESSFUL,
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType(),
                account.getCreatePerson(),
                account.getCreateDate()));

    }

}
