package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String cardNumber;
    private String transactionId;
    private Double amount;
    private Double balance;
    private Double fee;
    private String body;
    private String unitCurrency;
    private Integer status;
    private Integer transactionType;
    private String beneficiaryCardNumber;
    private String beneficiaryName;
    private String beneficiaryEmail;
    private String beneficiaryPhone;
    private String createPerson;
    private Date createDate;
    private CardNumberResponse card;
}
