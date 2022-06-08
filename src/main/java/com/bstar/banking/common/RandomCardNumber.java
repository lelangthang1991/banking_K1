package com.bstar.banking.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@AllArgsConstructor
public class RandomCardNumber {
    private String bankNumber;
    public RandomCardNumber() {
        this.bankNumber = RandomStringUtils.randomNumeric(12);
    }

    public String randomCardNumber(){
        return RandomStringUtils.randomNumeric(12);
    }

}
