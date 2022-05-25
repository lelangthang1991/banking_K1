package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePageAccount {
    private int pageNumber;
    private long pageSize;
    private int totalPages;
    private Object data;
}
