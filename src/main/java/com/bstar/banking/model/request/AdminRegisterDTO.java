package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegisterDTO {
    @NotBlank
    @Email
    private String email;
    @Min(1)
    @Max(5)
    private int accountType;
    @NotBlank
    @Size(min = 4, max = 4, message = "pincode requires 4 numbers")
    private String pinCode;
    @NotBlank
    @Size(max = 4, message = "pincode requires 4 numbers")
    private String confirmPinCode;
}
