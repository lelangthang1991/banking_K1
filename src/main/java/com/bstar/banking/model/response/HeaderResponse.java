package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.bstar.banking.utils.Utils.*;

@Data
@AllArgsConstructor
public class HeaderResponse {

    private UUID UID;
    private String date;
    public HeaderResponse() {
        this.UID = UUID.randomUUID();
        this.date = convertDateToString(Date.from(Instant.now()), YYMMDDMMSSSSS);
    }

}
