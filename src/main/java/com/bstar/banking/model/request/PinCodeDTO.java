package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class PinCodeDTO {
    @NotBlank
    private String accountNumber;
    @NotBlank
    private String pinCode;
}
