package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @Email
    @NotBlank
    private String email;
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String confirm;
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
    private Boolean isActivated;
    private String createPerson;
    private String updatePerson;
    private Date updateDate;
    private Date createDate;


}
