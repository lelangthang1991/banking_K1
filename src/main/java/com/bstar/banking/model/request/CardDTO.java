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
    @NotBlank
    private String cardNumber;
    private Double balance;
    @NotBlank
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
    private Double dailyAvailableTransfer;
    private Double monthlyAvailableTransfer;
}
