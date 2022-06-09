package com.bstar.banking.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bstar.banking.common.StatusCodeString.BAD_REQUEST;
import static com.bstar.banking.utils.Utils.FULL_DATE_FORMAT;
import static com.bstar.banking.utils.Utils.convertDateToString;

public class AsyncException implements AsyncUncaughtExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HttpServletResponse response;
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            Map<String, Object> data = new HashMap<>();
            data.put("statusCode", BAD_REQUEST);
            data.put("statusDescription", "Bad request");
            data.put("timestamp", convertDateToString(Date.from(Instant.now()), FULL_DATE_FORMAT));
            data.put("uuid", UUID.randomUUID());
            try {
                OutputStream out = response.getOutputStream();
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(out, data);
                out.flush();
            }catch (Exception e){
                log.error("Error: {}", e.getMessage());
            }
            log.error("=========================={}=======================", ex.getMessage(), ex);
            log.error("exception method: {}", method.getName());
            for (Object param : params) {
                log.error("Parameter value - {}", param);
            }
    }
}
