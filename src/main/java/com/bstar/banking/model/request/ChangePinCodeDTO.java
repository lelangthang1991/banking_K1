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
    @NotBlank(message = "Card number could not be null or empty")
    private String cardNumber;
    @NotBlank(message = "Pin code could not be null or empty")
    @Size(max = 4, message = "Pin code required 4 numbers")
    private String pinCode;
    @NotBlank(message = "New pin code could not be null or empty")
    private String newPinCode;
}
