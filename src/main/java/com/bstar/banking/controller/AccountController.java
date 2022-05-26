package com.bstar.banking.controller;

import com.bstar.banking.entity.Account;
import com.bstar.banking.model.request.AccountRequest;
import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.response.CommonResponse;
import com.bstar.banking.model.response.PinCodeResponse;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.AccountRepository;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.service.AccountService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static com.bstar.banking.common.AccountString.ACCOUNT_REGISTRATION_SCCESSFUL;
import static com.bstar.banking.common.UserString.PINCODE_DOES_NOT_MATCH;
import static com.bstar.banking.common.UserString.USER_NOT_FOUND;

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

    @GetMapping("/activated/find-by-keyword")
    public RestResponse<ResponsePageAccount> findAccountByKeywordActivated(@RequestParam(required = false) String keyword,
                                                                           @RequestParam boolean isActivated,
                                                                           @RequestParam int pageNumber,
                                                                           @RequestParam int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return accountService.findAccountByKeywordAndActivated(keyword, isActivated, pageRequest);
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

//        String email = authentication.getName();
//        if (!userRepository.existsById(authentication.getName())) {
//            return ResponseEntity.badRequest().body(USER_NOT_FOUND);
//        }
//
//        if (!bankrequest.getPinCode().equals(bankrequest.getConfirmPinCode())) {
//            return ResponseEntity.badRequest().body(PINCODE_DOES_NOT_MATCH);
//        }
//        Account account = new Account();
//
//        accountService.saveBankAccount(account, bankrequest, email);


        return accountService.bankregister(bankrequest, authentication);
    }
}
