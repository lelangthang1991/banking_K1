package com.bstar.banking.model.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestRequest<T> {

    private HeaderRequest header;
    @Valid
    private T body;
}
