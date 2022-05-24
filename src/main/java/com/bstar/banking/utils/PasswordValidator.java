package com.bstar.banking.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {

    private Pattern pattern;
    private Matcher matcher;

    /**
     * Be between 8 and 40 characters long
     * Contain at least one digit.
     * Contain at least one lower case character.
     * Contain at least one upper case character.
     * Contain at least on special character from [ @ # $ % ! . ].
     */
    private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,50})";

    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    public boolean validate(final String password) {

        matcher = pattern.matcher(password);
        return matcher.matches();

    }
}
