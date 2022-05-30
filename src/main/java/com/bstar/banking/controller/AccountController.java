package com.bstar.banking.controller;

import com.bstar.banking.model.request.ChangePinCodeDTO;
import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.request.RegisterBankAccountRq;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.AccountRepository;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/check-pin")
    public RestResponse<?> checkPinCode(@Valid @RequestBody PinCodeDTO pinCodeDTO, Authentication authentication) {
        return accountService.checkPinCode(pinCodeDTO, authentication);
    }

    @GetMapping("/get-by-email")
    public ResponseEntity<RestResponse<?>> getAccountByEmail(Authentication authentication) {
        return ResponseEntity.ok(accountService.findAccountByEmail(authentication.getName()));
    }

    @GetMapping("/get-page")
    public ResponseEntity<RestResponse<ResponsePageAccount>> getPageAccount(@RequestParam int pageNumber,
                                                                            @RequestParam int pageSize) {
        return ResponseEntity.ok(accountService.findPageAccount(PageRequest.of(pageNumber, pageSize)));
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
