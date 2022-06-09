package com.bstar.banking.controller.admin;

import com.bstar.banking.model.request.FilterTransactionDTO;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@PreAuthorize("hasAuthority('0')")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/transactions")
public class TransactionAdminController {

    private final TransactionService transactionService;

    @GetMapping("/get-page-transaction")
    public ResponseEntity<RestResponse<?>> getPageUserTransaction(@Valid FilterTransactionDTO transaction){
        return ResponseEntity.ok(transactionService.listAdminTransaction(transaction));
    }

}
