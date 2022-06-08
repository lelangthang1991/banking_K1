package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListTransactionDTO {

    @NotBlank
    @Size(min = 13, max = 16)
    private String cardNumber;
    private Integer cardType;
    private String transactionId;
    @Min(value = 1)
    @Max(value = 3)
    private Integer transactionType;
    private Double amount;
    private String body;
    private Double balance;
    private Boolean isActivated;
    private String unitCurrency;
    private String beneficiaryCardNumber;
    private String beneficiaryEmail;
    private Date createDate;
    private String createPerson;

}
