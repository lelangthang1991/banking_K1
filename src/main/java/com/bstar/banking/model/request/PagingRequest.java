package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingRequest {
    private Boolean isActivated;
    private String keyword;
    private String sortField;
    private String sortDir;
    @Min(0)
    private Integer pageNumber;
    @Min(0)
    private Integer pageSize;
}
