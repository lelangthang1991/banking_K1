package com.bstar.banking.controller;


import com.bstar.banking.model.request.DepositMoneyDTO;
import com.bstar.banking.model.request.ListTransactionDTO;
import com.bstar.banking.model.request.ListTransactionPagingRequest;
import com.bstar.banking.model.request.TransactionDTO;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.bstar.banking.common.StatusCodeString.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposit-money")
    public ResponseEntity<?> depositMoney(@Valid @RequestBody DepositMoneyDTO depositMoneyDTO, Authentication authentication) {

        RestResponse<?> response = transactionService.depositMoney(depositMoneyDTO, authentication);
        if (response.getStatusCode().equals(OK)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/withdraw-money")
    public ResponseEntity<?> withdrawMoney(@Valid @RequestBody DepositMoneyDTO transferMoneyDTO, Authentication authentication) {

        RestResponse<?> response = transactionService.withdrawMoney(transferMoneyDTO, authentication);
        if (response.getStatusCode().equals(OK)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/transfer-money")
    public ResponseEntity<?> transferMoney(@Valid @RequestBody TransactionDTO transferMoneyDTO, Authentication authentication) {

        RestResponse<?> response = transactionService.transferMoney(transferMoneyDTO, authentication);
        if (response.getStatusCode().equals(OK)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }


    @GetMapping("/list-transaction")
    public ResponseEntity<ResponsePageAccount> listTransaction(@Valid @RequestBody ListTransactionPagingRequest page, Authentication authentication) {

        PageRequest pageRequest = PageRequest.of(page.getPageNumber(), page.getPageSize());
        RestResponse<> response = transactionService.listTransaction(page, authentication, pageRequest);
        if (response.getStatusCode().equals(OK)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/list-all-transaction")
    public ResponseEntity<?> listAllTransaction(@Valid @RequestBody ListTransactionDTO listTransactionDTO, Authentication authentication) {

        RestResponse<?> response = transactionService.listAllTransaction(listTransactionDTO, authentication);
        if (response.getStatusCode().equals(OK)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

}
