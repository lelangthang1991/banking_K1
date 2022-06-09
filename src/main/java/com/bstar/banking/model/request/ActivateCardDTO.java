package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivateCardDTO {
    @NotBlank(message = "Card number could not be null or empty")
    private String cardNumber;
}
