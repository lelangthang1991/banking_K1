package com.bstar.banking.config;

import com.bstar.banking.exception.BusinessException;
import com.bstar.banking.model.response.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class ControllerAdviceHandler {
    private Logger logger;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<RestResponse<String>> handlerBusinessException(BusinessException be) {
        logger = LoggerFactory.getLogger("BusinessException");
        RestResponse<String> exceptionResponse = new RestResponse<String>(null);
        logger.error(be.toString());
        return ResponseEntity.ok(exceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<String>> handlerException(Exception ex) {
        logger = LoggerFactory.getLogger("Exception");
        String errorDetail = ex.getMessage();
        RestResponse<String> exceptionResponse = new RestResponse<String>(errorDetail);
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        logger.error(sw.toString());
        return ResponseEntity.ok(exceptionResponse);
    }
}
