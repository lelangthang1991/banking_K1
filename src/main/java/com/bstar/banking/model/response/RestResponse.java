package com.bstar.banking.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.bstar.banking.utils.Utils.YYMMDDMMSSSSS;
import static com.bstar.banking.utils.Utils.convertDateToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse<T> {
    private UUID UID;
    private String date;
    private String statusCode;
    private String statusDescription;
    private T data;

    public RestResponse(String statusCode, String statusDescription) {
        this.UID = UUID.randomUUID();
        this.date = convertDateToString(Date.from(Instant.now()), YYMMDDMMSSSSS);
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }

    public RestResponse(String statusCode, String statusDescription, T data) {
        this.UID = UUID.randomUUID();
        this.date = convertDateToString(Date.from(Instant.now()), YYMMDDMMSSSSS);
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
        this.data = data;
    }


}
