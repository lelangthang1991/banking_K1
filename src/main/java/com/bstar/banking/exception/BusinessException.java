package com.bstar.banking.exception;

import org.springframework.stereotype.Component;

@Component
public class BusinessException extends RuntimeException {
    public static final long serialVersionUID = 1L;
    private String errorCode;
    private String errorDesc;

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public BusinessException(String errorDesc) {
        super();
        this.errorCode = "10";
        this.errorDesc = errorDesc;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public BusinessException() {
    }

    public BusinessException(String errorCode, String errorDesc) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "errorCode='" + errorCode + '\'' +
                ", errorDesc='" + errorDesc + '\'' +
                '}';
    }
}