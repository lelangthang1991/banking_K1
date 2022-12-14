package com.bstar.banking.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bstar.banking.common.StatusCodeString.FORBIDDEN;
import static com.bstar.banking.utils.Utils.FULL_DATE_FORMAT;
import static com.bstar.banking.utils.Utils.convertDateToString;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        Map<String, Object> data = new HashMap<>();
        data.put("statusCode", FORBIDDEN);
        data.put("statusDescription", "Access denied");
        data.put("timestamp", convertDateToString(Date.from(Instant.now()), FULL_DATE_FORMAT));
        data.put("uuid", UUID.randomUUID());
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, data);
        out.flush();
    }
}
