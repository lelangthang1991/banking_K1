package com.bstar.banking.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CompareException extends RuntimeException{

    public CompareException(String message) {
        super(message);
    }
}
