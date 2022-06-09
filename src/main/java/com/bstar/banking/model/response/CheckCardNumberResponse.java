package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckCardNumberResponse {
    private String firstName;
    private String lastName;
    private Boolean isActivated;
}
