package com.bstar.banking.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Log4j2
public class AsyncException implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("=========================={}=======================", ex.getMessage(), ex);
        log.error("exception method: {}", method.getName());
        for (Object param : params) {
            log.error("Parameter value - {}", param);
        }
    }
}
