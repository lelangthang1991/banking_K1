package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordDTO {
    @NotBlank(message = "Email could not be null or empty")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "New password could not be null or empty")
    private String newPassword;
    @NotBlank(message = "Verify code could not be null or empty")
    private String verifyCode;

}
