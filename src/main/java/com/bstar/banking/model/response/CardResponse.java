package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {
    private String cardNumber;
    private Double balance;
    private Integer cardType;
    private Boolean isActivated;
    private String createPerson;
    private String updatePerson;
    private Date updateDate;
    private Date createDate;
    private Integer level;
    private Double dailyLimitAmount;
    private Double monthlyLimitAmount;
}


