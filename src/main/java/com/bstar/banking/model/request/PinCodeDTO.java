package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PinCodeDTO {
    @NotBlank
    private String cardNumber;
    @NotBlank
    private String pinCode;
}
