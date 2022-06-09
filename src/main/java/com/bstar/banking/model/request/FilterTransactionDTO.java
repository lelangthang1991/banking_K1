package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterTransactionDTO {
    @Pattern(regexp = "^[0-9]+$", message = "Card number value must be positive integer")
    private String cardNumber;
    private String createPerson;
    private String updatePerson;
    private Integer transactionType;
    private String sortField;
    private String sortDir;
    @NotNull
    @Min(0)
    private Integer pageNumber;
    @NotNull
    @Min(0)
    private Integer pageSize;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
}
