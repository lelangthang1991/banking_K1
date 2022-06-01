package com.bstar.banking.controller.admin;

import com.bstar.banking.model.request.AccountDTO;
import com.bstar.banking.model.request.PagingRequest;
import com.bstar.banking.model.request.RegisterBankAccountRq;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.AccountService;
import org.springframework.data.domain.PageRequest;
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
        return accountService.findPageAccount(PageRequest.of(page.getPageNumber(), page.getPageSize()));
    }

    @GetMapping("/{accountNumber}")
    public RestResponse<?> getAccountDetail(@PathVariable("accountNumber") String accountNumber) {
        return accountService.findAccountByAccountNumber(accountNumber);
    }

    @GetMapping("/activated/find-by-keyword")
    public RestResponse<ResponsePageAccount> findAccountByKeywordActivated(@Valid PagingRequest page,
                                                                           @RequestParam boolean isActivated) {
        PageRequest pageRequest = PageRequest.of(page.getPageNumber(), page.getPageSize());
        return accountService.findAccountByKeywordAndActivated(page.getKeyword(), isActivated, pageRequest);
    }

    @GetMapping("/find-by-keyword")
    public RestResponse<ResponsePageAccount> findAccountByKeyword(@Valid PagingRequest page) {
        PageRequest pageRequest = PageRequest.of(page.getPageNumber(), page.getPageSize());
        return accountService.findAccountByKeyword(page.getKeyword(), pageRequest);
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
    public ResponseEntity<RestResponse<?>> bankAdminRegister(@Valid @RequestBody RegisterBankAccountRq registerBankAccountRq,
                                                             Authentication authentication) {
        return ResponseEntity.ok(accountService.bankRegister(registerBankAccountRq, authentication));
    }
}
