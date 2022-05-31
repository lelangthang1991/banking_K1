package com.bstar.banking.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data

@AllArgsConstructor
public class RandomBankNumber {
    public RandomBankNumber() {
        this.banknumber = RandomStringUtils.randomNumeric(13);
    }

    private String banknumber;


    public String randomBankNumber() {
        return RandomStringUtils.randomNumeric(13);
    }

}
