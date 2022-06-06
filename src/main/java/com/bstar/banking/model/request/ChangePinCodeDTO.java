package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePinCodeDTO {
    @NotBlank
    private String cardNumber;
    @NotBlank
    private String pinCode;
    @NotBlank
    private String newPinCode;
}
