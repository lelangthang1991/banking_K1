package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecentralizationDTO {
    @Email
    @NotBlank
    private String email;
    @NotNull
    @Min(0)
    @Max(1)
    private Integer role;
}
