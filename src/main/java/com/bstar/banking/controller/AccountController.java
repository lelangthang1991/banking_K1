package com.bstar.banking.controller;

import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.response.CommonResponse;
import com.bstar.banking.model.response.PinCodeResponse;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.AccountService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

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
    public RestResponse<CommonResponse> getAccountByEmail(Authentication authentication){
        return accountService.findAccountByEmail(authentication.getName());
    }
}
