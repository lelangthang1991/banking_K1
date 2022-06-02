package com.bstar.banking.controller;

import com.bstar.banking.model.request.ChangePinCodeDTO;
import com.bstar.banking.model.request.PagingRequest;
import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.request.RegisterBankAccountRq;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/check-pin")
    public RestResponse<?> checkPinCode(@Valid @RequestBody PinCodeDTO pinCodeDTO, Authentication authentication) {
        return accountService.checkPinCode(pinCodeDTO, authentication);
    }

    @GetMapping("/get-by-email")
    public ResponseEntity<RestResponse<?>> getAccountByEmail(Authentication authentication) {
        return ResponseEntity.ok(accountService.findAccountByEmail(authentication.getName()));
    }

    @GetMapping("/get-page")
    public ResponseEntity<RestResponse<ResponsePageAccount>> getPageAccount(@Valid PagingRequest page) {
        return ResponseEntity.ok(accountService.findPageAccount(page));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<RestResponse<?>> getAccountDetail(@PathVariable("accountNumber") String accountNumber) {
        return ResponseEntity.ok(accountService.findAccountByAccountNumber(accountNumber));
    }

    @PutMapping("/change-pin-code")
    public ResponseEntity<RestResponse<?>> changePinCode(@Valid @RequestBody ChangePinCodeDTO changePinCodeDTO,
                                                         Authentication authentication) {
        return ResponseEntity.ok(accountService.changePinCode(changePinCodeDTO, authentication));
    }

    @PostMapping("/bank-register")
    public ResponseEntity<RestResponse<?>> bankRegister(@Valid @RequestBody RegisterBankAccountRq registerBankAccountRq,
                                                        Authentication authentication) {
        return ResponseEntity.ok(accountService.bankRegister(registerBankAccountRq, authentication));
    }
}
