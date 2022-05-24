package com.bstar.banking.model.request;

import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.bstar.banking.utils.Utils.YYMMDDMMSSSSS;
import static com.bstar.banking.utils.Utils.convertDateToString;


@Data
public class HeaderRequest {
    private UUID requestUID;
    private String date;

    public HeaderRequest() {
        this.requestUID = UUID.randomUUID();
        this.date = convertDateToString(Date.from(Instant.now()), YYMMDDMMSSSSS);
    }
}
