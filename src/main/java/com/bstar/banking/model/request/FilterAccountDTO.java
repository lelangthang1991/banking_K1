package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterAccountDTO {
    @Pattern(regexp = "^[0-9]+$", message="the value must be positive integer")
    private String accountNumber;
    @Pattern(regexp = "^[0-9]+$", message="the value must be positive integer")
    private String pinCode;
    private Integer accountType;
    private String sortField;
    private String sortDir;
    @Min(0)
    private Integer pageNumber;
    @Min(0)
    private Integer pageSize;
}
