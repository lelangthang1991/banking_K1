package com.bstar.banking.controller;


import com.bstar.banking.model.request.DepositMoneyDTO;
import com.bstar.banking.model.request.TransactionDTO;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposit-money")
    public ResponseEntity<?> depositMoney(@Valid @RequestBody DepositMoneyDTO depositMoneyDTO, Authentication authentication) {

        RestResponse<?> response = transactionService.depositMoney(depositMoneyDTO, authentication);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw-money")
    public ResponseEntity<?> withdrawMoney(@Valid @RequestBody DepositMoneyDTO transferMoneyDTO, Authentication authentication) {

        RestResponse<?> response = transactionService.withdrawMoney(transferMoneyDTO, authentication);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer-money")
    public ResponseEntity<?> transferMoney(@Valid @RequestBody TransactionDTO transferMoneyDTO, Authentication authentication) {

        RestResponse<?> response = transactionService.transferMoney(transferMoneyDTO, authentication);
        return ResponseEntity.ok(response);
    }

}
