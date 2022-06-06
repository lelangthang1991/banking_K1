package com.bstar.banking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class User {
    @Id
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Integer role;
    private String verifyCode;
    private Date dob;
    private Integer gender;
    private String address;
    private String phone;
    private Boolean isActivated;
    private String createPerson;
    private String updatePerson;
    private Date updateDate;
    private Date createDate;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    List<Session> sessions;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    List<Card> cards;


}
