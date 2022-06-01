package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferMoneyResponse {

    private Integer transactionId;
    private Double amount;
    private Double balance;
    private String body;
    private Integer transactionType;
    private String beneficiaryAccountNumber;
    private String beneficiaryName;
    private String beneficiaryEmail;
    private String beneficiaryPhone;
    private String createPerson;
    private Date createDate;
}
