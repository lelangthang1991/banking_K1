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
    @NotBlank(message = "Owner number could not be null or empty")
    @Size(min = 13, max = 20)
    private String ownerNumber;
    private String transactionId;
    @Min(value = 0, message = "Amount has the smallest value of 0")
    private Double amount;
    private Double balance;
    private Double fee;
    @NotBlank(message = "Pin code could not be null or empty")
    @Size(max = 4, message = "Pin code required 4 numbers")
    private String pinCode;
    @NotBlank(message = "Body could not be null or empty")
    @Size(max = 200, message = "Body has the maximum value of 200")
    private String body;
    private String unitCurrency;
    private Integer status;
    private Integer transactionType;
    @NotBlank
    @Size(min = 13, max = 20)
    private String beneficiaryCardNumber;
    private String beneficiaryName;
    private String beneficiaryEmail;
    private String beneficiaryPhone;
    private String createPerson;
    private Date createDate;

}
