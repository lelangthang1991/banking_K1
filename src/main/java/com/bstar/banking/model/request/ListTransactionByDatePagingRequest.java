package com.bstar.banking.model.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import java.util.Date;

@Data
public class ListTransactionByDatePagingRequest {
    private String cardNumber;
    private Integer transactionType;
    private String sortField;
    private String sortDir;
    @Min(0)
    private Integer pageNumber;
    @Min(0)
    private Integer pageSize;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
}
