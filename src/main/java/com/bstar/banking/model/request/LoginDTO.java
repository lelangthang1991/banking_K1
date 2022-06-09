package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO implements Serializable {
    @NotBlank(message = "Email could not be null or empty")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password could not be null or empty")
    private String password;
}
