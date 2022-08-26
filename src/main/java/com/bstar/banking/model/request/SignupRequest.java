package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank(message = "Email could not be null or empty")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Invalid email format")
    private String email;
    @Pattern(regexp = "^(=.*[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$", message = "Invalid password format")
    private String password;
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$", message = "Invalid confirm password format")
    private String confirm;
    @NotBlank(message = "First name could not be null or empty")
    private String firstName;
    @NotBlank(message = "Last name could not be null or empty")
    private String lastName;
    private Date dob;
    private Integer gender;
    @NotBlank(message = "Address could not be null or empty")
    private String address;
    @Size(min = 10, max = 10, message = "Phone must be number 10 characters")
    private String phone;
    private Boolean isActivated;
    private String createPerson;
    private String updatePerson;
    private Date updateDate;
    private Date createDate;


}
