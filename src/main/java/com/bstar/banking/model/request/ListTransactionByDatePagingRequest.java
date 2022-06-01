package com.bstar.banking.model.request;

import jdk.jfr.Timestamp;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class ListTransactionByDatePagingRequest {
    private String accountNumber;
    private Integer transactionType;
    private String keyword;
    private String sort;
    @Min(0)
    private Integer pageNumber;
    @Min(0)
    private Integer pageSize;

    private Date startDate;

    private Date endDate;
}
