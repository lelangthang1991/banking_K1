package com.bstar.banking.controller.admin;

import com.bstar.banking.model.request.*;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@PreAuthorize("hasAuthority('0')")
@RequestMapping("/api/v1/admin/accounts")
@RestController
public class AccountAdminController {
    private final AccountService accountService;

    public AccountAdminController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/get-page")
    public RestResponse<ResponsePageAccount> getPageAccount(@Valid PagingRequest page) {
        return accountService.findPageAccount(page);
    }

    @GetMapping("/{accountNumber}")
    public RestResponse<?> getAccountDetail(@PathVariable("accountNumber") String accountNumber) {
        return accountService.findAccountByAccountNumber(accountNumber);
    }

    @GetMapping("/activated/find-by-keyword")
    public RestResponse<ResponsePageAccount> findAccountByKeywordActivated(@Valid PagingRequest page) {
        return accountService.findAccountByKeywordAndActivated(page);
    }

    @GetMapping("/find-by-keyword")
    public RestResponse<ResponsePageAccount> findAccountByKeyword(@Valid PagingRequest page) {
        return accountService.findAccountByKeyword(page);
    }

    @PutMapping
    public ResponseEntity<RestResponse<?>> accountUpdate(@Valid @RequestBody AccountDTO accountDTO, Authentication authentication) {
        return ResponseEntity.ok(accountService.accountUpdate(accountDTO, authentication));
    }

    @DeleteMapping("/{accountNumber}")
    public RestResponse<?> accountDisabled(@PathVariable("accountNumber") String accountNumber) {
        return accountService.accountDisabled(accountNumber);
    }

    @PostMapping("/bank-register")
    public ResponseEntity<RestResponse<?>> bankAdminRegister(@Valid @RequestBody AdminRegisterDTO registerDTO,
                                                             Authentication authentication) {
        return ResponseEntity.ok(accountService.adminBankRegister(registerDTO, authentication));
    }

    @GetMapping("/get-all-account")
    public ResponseEntity<RestResponse<?>> findAllAccountFiltered(@Valid FilterAccountDTO accountDTO) {
        return ResponseEntity.ok(accountService.findAllAccountFiltered(accountDTO));
    }

    @PostMapping("/activate-account")
    public ResponseEntity<RestResponse<?>> activatedAccount(@Valid @RequestBody ActivateAccountDTO accountDTO) {
        return ResponseEntity.ok(accountService.activatedAccount(accountDTO));
    }
}
