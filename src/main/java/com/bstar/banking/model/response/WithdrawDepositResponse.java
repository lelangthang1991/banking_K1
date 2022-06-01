package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawDepositResponse {

    private Integer transactionId;
    private Double amount;
    private Double balance;
    private Integer transactionType;
    private String createPerson;
    private Date createDate;
}
