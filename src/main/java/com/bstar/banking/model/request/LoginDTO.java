package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

import static com.bstar.banking.common.JwtString.INCORRECT_EMAIL_FORMAT;
import static com.bstar.banking.common.JwtString.INCORRECT_EMAIL_OR_PASSWORD;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO implements Serializable {
    @NotBlank(message = INCORRECT_EMAIL_OR_PASSWORD)
    @Email(message = INCORRECT_EMAIL_FORMAT)
    private String email;
    @NotBlank(message = INCORRECT_EMAIL_OR_PASSWORD)
    private String password;
}
