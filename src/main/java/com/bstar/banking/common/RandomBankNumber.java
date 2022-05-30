package com.bstar.banking.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@AllArgsConstructor
public class RandomBankNumber {
    private String bankNumber;
    public RandomBankNumber() {
        this.bankNumber = RandomStringUtils.randomNumeric(13);
    }

    public String randomBankNumber(){
        return RandomStringUtils.randomNumeric(13);
    }

}
