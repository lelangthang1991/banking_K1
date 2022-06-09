package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailDTO {
    @NotBlank(message = "Receiver could not be null or empty")
    private String receiver;
    @NotBlank(message = "Verify code sender could not be null or empty")
    private String verifyCode;
    @NotBlank(message = "Password could not be null or empty")
    private String password;
}
