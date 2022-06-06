package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePageCard {
    private int pageNumber;
    private long pageSize;
    private int totalPages;
    private long totalItem;
    private Object data;
}
