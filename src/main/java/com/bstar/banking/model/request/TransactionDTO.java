package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    @NotBlank
    @Size(min = 13, max = 16)
    private String ownerNumber;
    private String transactionId;
    @Min(value = 0)
    private Double amount;
    private Double balance;
    @NotBlank
    @Size(max = 4)
    private String pinCode;
    @NotBlank
    @Size(max = 200)
    private String body;
    private String unitCurrency;
    private Integer status;
    private Integer transactionType;
    @NotBlank
    @Size(min = 13, max = 16)
    private String beneficiaryCardNumber;
    private String beneficiaryName;
    private String beneficiaryEmail;
    private String beneficiaryPhone;
    private String createPerson;
    private Date createDate;
}
