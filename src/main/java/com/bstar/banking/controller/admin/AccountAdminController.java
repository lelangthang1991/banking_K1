package com.bstar.banking.controller.admin;

import com.bstar.banking.model.request.AccountDTO;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.AccountService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("*")
@RequestMapping("/api/v1/admin/accounts")
@RestController
public class AccountAdminController {

    private final AccountService accountService;

    public AccountAdminController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/get-page")
    public RestResponse<ResponsePageAccount> getPageAccount(@RequestParam int pageNumber,
                                                            @RequestParam int pageSize) {
        return accountService.findPageAccount(PageRequest.of(pageNumber, pageSize));
    }

    @GetMapping("/{accountNumber}")
    public RestResponse<?> getAccountDetail(@PathVariable("accountNumber") String accountNumber) {
        return accountService.findAccountByAccountNumber(accountNumber);
    }

    @GetMapping("/activated/find-by-keyword")
    public RestResponse<ResponsePageAccount> findAccountByKeywordActivated(@RequestParam(required = false) String keyword,
                                                                           @RequestParam boolean isActivated,
                                                                           @RequestParam int pageNumber,
                                                                           @RequestParam int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return accountService.findAccountByKeywordAndActivated(keyword, isActivated, pageRequest);
    }

    /**
     * @param keyword
     * @param pageNumber
     * @param pageSize
     * @return page Account with keyword
     * http://localhost:8080/api/v1/accounts/find-by-keyword?keyword=123456789&page=1&size=5
     */
    @GetMapping("/find-by-keyword")
    public RestResponse<ResponsePageAccount> findAccountByKeyword(@RequestParam(required = false) String keyword,
                                                                  @RequestParam int pageNumber,
                                                                  @RequestParam int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return accountService.findAccountByKeyword(keyword, pageRequest);
    }

    @PutMapping
    public ResponseEntity<RestResponse<?>> accountUpdate(@Valid @RequestBody AccountDTO accountDTO, Authentication authentication) {
        return ResponseEntity.ok(accountService.accountUpdate(accountDTO, authentication));
    }

    @DeleteMapping("/{accountNumber}")
    public RestResponse<?> accountDisabled(@PathVariable("accountNumber") String accountNumber) {
        return accountService.accountDisabled(accountNumber);
    }
}
