package com.bstar.banking.config;

import com.bstar.banking.exception.AsyncException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

import static com.bstar.banking.common.StatusCodeString.OK;
import static com.bstar.banking.utils.Utils.FULL_DATE_FORMAT;
import static com.bstar.banking.utils.Utils.convertDateToString;

@Configuration
@EnableAsync
public class AsynConfig extends AsyncConfigurerSupport {
    private final Logger log = LoggerFactory.getLogger(AsynConfig.class);
    @Autowired
    private HttpServletResponse response;

    @Override
    public Executor getAsyncExecutor() {
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json");
            Map<String, Object> data = new HashMap<>();
            data.put("statusCode", OK);
            data.put("statusDescription", "Transaction successfully");
            data.put("timestamp", convertDateToString(Date.from(Instant.now()), FULL_DATE_FORMAT));
            data.put("uuid", UUID.randomUUID());
            OutputStream out = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, data);
            out.flush();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(4);
        executor.setKeepAliveSeconds(60);
        executor.setQueueCapacity(10000);
        executor.setThreadNamePrefix("transaction-task-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncException();
    }
}
