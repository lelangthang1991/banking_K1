package com.bstar.banking.config;

import com.bstar.banking.exception.CompareException;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.exception.PinCodeException;
import com.bstar.banking.model.response.ExceptionResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Log4j2
@ControllerAdvice
public class ControllerAdviceHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handlerNotFoundException(NotFoundException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse("404", ex.getMessage()));
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse("400", "Please enter a valid value"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("IllegalArgumentException " + ex.getMessage());
        ex.getStackTrace();
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse("400", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handlerException(Exception ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse("400", ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handlerAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse("400", "UnAuthentication"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handlerAccessDeniedException(AccessDeniedException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse("400", "Access denied"));
    }

    @ExceptionHandler(CompareException.class)
    public ResponseEntity<Object> handlerCompareException(CompareException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse("400", ex.getMessage()));
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        ex.printStackTrace();
        List<Map<String, String>> details = new ArrayList<>();
        AtomicReference<String> errMessage = new AtomicReference<>("");
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> {
                    Map<String, String> detail = new HashMap<>();
                    detail.put("objectName", fieldError.getObjectName());
                    detail.put("field", fieldError.getField());
                    errMessage.set(fieldError.getDefaultMessage());
                    detail.put("rejectedValue", "" + fieldError.getRejectedValue());
                    detail.put("errorMessage", fieldError.getDefaultMessage());
                    details.add(detail);
                });
        logger.error(ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse("400", errMessage.toString(), details));
    }

    @ExceptionHandler(PinCodeException.class)
    public ResponseEntity<?> handlerPinCodeException(PinCodeException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse("400", ex.getMessage()));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handlerBadCredentialsException(BadCredentialsException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse("400", ex.getMessage()));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handlerDisabledException(DisabledException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse("400", ex.getMessage()));
    }
}
