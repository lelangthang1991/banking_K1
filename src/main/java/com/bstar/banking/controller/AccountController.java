package com.bstar.banking.controller;

import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.response.PinCodeResponse;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/check-pin")
    public RestResponse<PinCodeResponse> checkPinCode(@Valid @RequestBody PinCodeDTO pinCodeDTO) {
        return accountService.checkPinCode(pinCodeDTO);
    }
}
