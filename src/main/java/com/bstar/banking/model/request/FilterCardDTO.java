package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterCardDTO {
    @Pattern(regexp = "^[0-9]+$", message = "the value must be positive integer")
    private String cardNumber;
    private String createPerson;
    private String updatePerson;
    private String sortField;
    private String sortDir;
    @NotNull
    @Min(0)
    private Integer pageNumber;
    @NotNull
    @Min(0)
    private Integer pageSize;
}
