package com.bstar.banking.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Date dob;
    private Boolean role;
    private Integer gender;
    private String address;
    private String phone;
    private Boolean isActivated;
    private String create_person;
    private String update_person;
    private Date update_date;
    private Date create_date;
}
