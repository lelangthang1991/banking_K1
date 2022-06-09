package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {
    @NotBlank(message = "Password number could not be null or empty")
    private String password;
    @NotBlank(message = "New password number could not be null or empty")
    private String newPassword;
    @NotBlank(message = "Confirm new password number could not be null or empty")
    private String confirmNewPassword;
}
