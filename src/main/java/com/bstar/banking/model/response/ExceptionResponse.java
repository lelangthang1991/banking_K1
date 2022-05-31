package com.bstar.banking.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.bstar.banking.utils.Utils.FULL_DATE_FORMAT;
import static com.bstar.banking.utils.Utils.convertDateToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
    private String statusDescription;
    private String statusCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private String date;
    private UUID uuid;

    private Object data;

    public ExceptionResponse(String errorCode, String errorMessage) {
        this.uuid = UUID.randomUUID();
        this.date = convertDateToString(Date.from(Instant.now()), FULL_DATE_FORMAT);
        this.statusDescription = errorMessage;
        this.statusCode = errorCode;
    }

    public ExceptionResponse(String errorCode, String errorMessage, Object data) {
        this.uuid = UUID.randomUUID();
        this.date = convertDateToString(Date.from(Instant.now()), FULL_DATE_FORMAT);
        this.statusDescription = errorMessage;
        this.statusCode = errorCode;
        this.data = data;
    }
}

