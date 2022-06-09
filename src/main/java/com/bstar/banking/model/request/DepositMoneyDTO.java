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

    @NotBlank(message = "Card number could not be null or empty")
    @Size(min = 13, max = 20)
    private String cardNumber;
    @Min(value = 50000)
    private Double amount;
    @NotBlank(message = "Pin code could not be null or empty")
    @Size(max = 4)
    private String pinCode;
}
