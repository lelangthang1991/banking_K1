package com.bstar.banking.controller;


import com.bstar.banking.model.request.DepositMoneyDTO;
import com.bstar.banking.model.request.ListTransactionByDatePagingRequest;
import com.bstar.banking.model.request.TransactionDTO;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok(transactionService.depositMoney(depositMoneyDTO, authentication));
    }

    @PostMapping("/withdraw-money")
    public ResponseEntity<?> withdrawMoney(@Valid @RequestBody DepositMoneyDTO transferMoneyDTO, Authentication authentication) {
        return ResponseEntity.ok(transactionService.withdrawMoney(transferMoneyDTO, authentication));
    }

    @PostMapping("/transfer-money")
    public ResponseEntity<?> transferMoney(@Valid @RequestBody TransactionDTO transferMoneyDTO, Authentication authentication) {
        return ResponseEntity.ok(transactionService.transferMoney(transferMoneyDTO, authentication));
    }


    @GetMapping("/list-transaction-by-type")
    public RestResponse<ResponsePageAccount> listTransaction(@Valid ListTransactionByDatePagingRequest page, Authentication authentication) {
        return transactionService.listTransactionByAccountAndType(page, authentication);
    }

    @GetMapping("/list-all-transaction")
    public RestResponse<ResponsePageAccount> listAllTransaction(@Valid ListTransactionByDatePagingRequest page, Authentication authentication) {
        return transactionService.listAllTransactionByAccount(page, authentication);
    }


}
