package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<B> implements Serializable {
    private static final long serialVersionUID = 1L;

    private HeaderResponse header;
    private B body;
}
