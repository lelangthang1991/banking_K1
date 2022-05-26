package com.bstar.banking.controller;

import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.response.CommonResponse;
import com.bstar.banking.model.response.PinCodeResponse;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.AccountService;
import org.springframework.data.domain.PageRequest;
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

    @PostMapping("/check-pin")
    public RestResponse<PinCodeResponse> checkPinCode(@Valid @RequestBody PinCodeDTO pinCodeDTO) {
        return accountService.checkPinCode(pinCodeDTO);
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

    @PostMapping("/activated/find-by-keyword")
    public RestResponse<ResponsePageAccount> findAccountByKeywordActivated(@RequestParam(required = false) String keyword,
                                                                           @RequestParam boolean isActivated,
                                                                           @RequestParam int pageNumber,
                                                                           @RequestParam int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return accountService.findAccountByKeywordAndActivated(keyword, isActivated, pageRequest);
    }

    @GetMapping("/find-by-email")
    public RestResponse<CommonResponse> findAccountByEmail(Authentication authentication){
        return accountService.findAccountByEmail(authentication.getName());
    }

    @GetMapping("/find-page")
    public RestResponse<ResponsePageAccount> findPageAccount(@RequestParam int pageNumber,
                                                        @RequestParam int pageSize){
        return accountService.findPageAccount(PageRequest.of(pageNumber, pageSize));
    }
}
