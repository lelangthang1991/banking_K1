package com.bstar.banking.model.request;

import com.bstar.banking.model.response.CardResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    private Integer role;
    private Integer gender;
    private String address;
    private String phone;
    private Boolean isActivated;
    private String createPerson;
    private String updatePerson;
    private Date updateDate;
    private Date createDate;
    private List<CardResponse> cards;
}
