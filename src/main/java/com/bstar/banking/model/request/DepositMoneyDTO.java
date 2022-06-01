package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositMoneyDTO {

    @NotBlank
    @Size(min = 13, max = 16)
    private String accountNumber;

    @Min(value = 0)
    private Double amount;

    @NotBlank
    @Size(max = 4)
    private String pinCode;
}
