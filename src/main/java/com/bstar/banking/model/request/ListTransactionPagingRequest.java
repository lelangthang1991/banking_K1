package com.bstar.banking.model.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ListTransactionPagingRequest {
    private String cardNumber;
    private Integer transactionType;
    private String keyword;
    private String sort;
    @Min(0)
    private Integer pageNumber;
    @Min(0)
    private Integer pageSize;
}