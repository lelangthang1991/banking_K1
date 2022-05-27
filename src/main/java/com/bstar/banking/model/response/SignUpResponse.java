package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor

public class SignUpResponse implements Serializable {
    private String statusCode;
    private String statusDescription;
    public SignUpResponse(String status, String message) {
        this.statusCode = status;
        this.statusDescription = message;
    }
}
