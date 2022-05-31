package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePageAccount {
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalItem;
    private Object data;
}
