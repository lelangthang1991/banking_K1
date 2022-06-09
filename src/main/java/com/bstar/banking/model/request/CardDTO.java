package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    @NotBlank(message = "Card number could not be null or empty")
    private String cardNumber;
    private Double balance;
    @NotBlank(message = "Pin code could not be null or empty")
    private String pinCode;
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
