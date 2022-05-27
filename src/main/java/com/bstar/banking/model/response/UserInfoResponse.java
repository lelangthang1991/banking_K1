package com.bstar.banking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private String statusCode;
    private String statusDescription;

    private String email;
    private String firstName;
    private String lastName;
    private Date dob;
    private Integer gender;
    private String address;
    private String phone;
    private Date update_date;
    private Date create_date;

    public UserInfoResponse(String status, String message) {
        this.statusCode = status;
        this.statusDescription = message;
    }
}
