package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordResponse {
    private String statusCode;
    private String statusDescription;
    private String email;

    public ForgotPasswordResponse(String status, String message) {
        this.statusCode = status;
        this.statusDescription = message;
    }
}
