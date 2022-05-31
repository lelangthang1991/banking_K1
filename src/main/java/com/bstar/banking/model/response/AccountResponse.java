package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private String statusCode;
    private String statusDescription;
    private String accountNumber;
    private Double balance;
    private Integer accountType;
    private String createPerson;
    private Date createDate;

    public AccountResponse(String status, String message) {
        this.statusCode = status;
        this.statusDescription = message;
    }
}
