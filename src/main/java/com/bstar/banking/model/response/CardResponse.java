package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {
    private String accountNumber;
    private Double balance;
    private Integer accountType;
    private Boolean isActivated;
}
