package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PinCodeResponse {
    private String statusCode;
    private String statusDescription;
}
