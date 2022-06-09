package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecentralizationDTO {
    @NotBlank(message = "Email could not be null or empty")
    @Email(message = "Invalid email format")
    private String email;
    @NotNull(message = "Role could not be null")
    @Min(0)
    @Max(1)
    private Integer role;
}
