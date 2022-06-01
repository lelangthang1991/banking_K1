package com.bstar.banking.model.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ListTransactionPagingRequest {
    @NotBlank
    @Size(min = 13, max = 16)
    private String accountNumber;
    private Integer transactionType;
    private String keyword;
    private String sort;
    @Min(0)
    private Integer pageNumber;
    @Min(0)
    private Integer pageSize;
}
