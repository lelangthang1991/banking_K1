package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private Date dob;
    private Integer gender;
    @NotBlank
    private String address;
    @Size(min = 10, max = 14, message = "phone must be number and between 10 and 20 characters")
    private String phone;
    private String updatePerson;
    private Date updateDate;

}
