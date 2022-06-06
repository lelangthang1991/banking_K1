package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterUserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private Integer role;
    private String sortField;
    private String sortDir;
    @Min(0)
    private Integer pageNumber;
    @Min(0)
    private Integer pageSize;
}
