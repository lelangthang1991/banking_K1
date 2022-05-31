package com.bstar.banking.model.request;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class PagingRequest {
    private String keyword;
    private String sort;
    @Min(0)
    private Integer pageNumber;
    @Min(0)
    private Integer pageSize;
}
