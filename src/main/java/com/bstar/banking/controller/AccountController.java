package com.bstar.banking.controller;

import com.bstar.banking.model.request.AccountRequest;
import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.response.CommonResponse;
import com.bstar.banking.model.response.PinCodeResponse;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.AccountRepository;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    AccountRepository accountrepo;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/check-pin")
    public RestResponse<PinCodeResponse> checkPinCode(@Valid @RequestBody PinCodeDTO pinCodeDTO, Authentication authentication) {
        return accountService.checkPinCode(pinCodeDTO, authentication);
    }

    @GetMapping("/get-by-email")
    public RestResponse<CommonResponse> getAccountByEmail(Authentication authentication) {
        return accountService.findAccountByEmail(authentication.getName());
    }

    @GetMapping("/get-page")
    public RestResponse<ResponsePageAccount> getPageAccount(@RequestParam int pageNumber,
                                                            @RequestParam int pageSize) {
        return accountService.findPageAccount(PageRequest.of(pageNumber, pageSize));
    }

    @GetMapping("/{accountNumber}")
    public RestResponse<CommonResponse> getAccountDetail(@PathVariable("accountNumber") String accountNumber) {
        return accountService.findAccountByAccountNumber(accountNumber);
    }


    @PostMapping("/register")
    public ResponseEntity<?> bankregister(@Valid @RequestBody AccountRequest bankrequest, Authentication authentication) {


        return accountService.bankregister(bankrequest, authentication);
    }
}
