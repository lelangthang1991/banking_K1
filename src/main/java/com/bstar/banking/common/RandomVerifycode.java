package com.bstar.banking.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@AllArgsConstructor
public class RandomVerifycode {


    private String verifycode;


    public RandomVerifycode() {
        this.verifycode = RandomStringUtils.randomAlphabetic(10);

    }

    public String Random() {
        return (RandomStringUtils.randomAlphabetic(10));
    }
}
