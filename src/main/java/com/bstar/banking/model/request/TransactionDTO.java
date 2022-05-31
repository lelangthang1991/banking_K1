package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO implements Serializable {
    @NotNull
    private Integer transactionId;
    private String transferNumber;
    private Double amount;
    private String pinCode;
    private String unitCurrency;
    private Integer status;
    private Integer transactionType;
    private String beneficiaryAccountNumber;
    private String beneficiaryName;
    private String beneficiaryEmail;
    private String beneficiaryPhone;
    private String createPerson;
}
