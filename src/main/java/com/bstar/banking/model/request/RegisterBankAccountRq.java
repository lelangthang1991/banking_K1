package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterBankAccountRq {

    @Min(1)
    @Max(5)
    private int accountType;
    @NotBlank
    @Size(min=4,max = 4, message = "pinCode requires 4 numbers")
    private String pinCode;
    @NotBlank
    @Size(max = 4,message = "pinCode requires 4 numbers")
    private String confirmPinCode;


}
