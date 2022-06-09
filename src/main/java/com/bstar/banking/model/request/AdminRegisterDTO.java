package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegisterDTO {
    @NotBlank(message = "Email could not be null or empty")
    @Email(message = "Invalid email format")
    private String email;
    @Min(value = 1, message = "Card type has the smallest value of 0")
    @Max(value = 1, message = "Card type has the maximum value of 5")
    private int cardType;
    @NotBlank(message = "Pin Code could not be null or empty")
    @Size(min = 4, max = 4, message = "Pin code required 4 numbers")
    private String pinCode;
    @NotBlank(message = "Confirm pin code could not be null or empty")
    @Size(max = 4, message = "Pin code required 4 numbers")
    private String confirmPinCode;
}
