package com.bstar.banking.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@Getter
@Setter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    private String statusCode;
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String statusCode,String message) {
        super(message);
        this.statusCode = statusCode;
    }


}
