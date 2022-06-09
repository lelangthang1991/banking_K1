package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePinCodeDTO {
    @NotBlank
    private String cardNumber;
    @NotBlank
    @Size(max = 4)
    private String pinCode;
    @NotBlank
    private String newPinCode;
}
