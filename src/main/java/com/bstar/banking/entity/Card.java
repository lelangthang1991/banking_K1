package com.bstar.banking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Card {
    @Id
    private String cardNumber;
    private Double balance;
    private String pinCode;
    private Integer cardType;
    private Boolean isActivated;
    private String createPerson;
    private String updatePerson;
    private Date updateDate;
    private Date createDate;
    private Integer level;
    private Double dailyLimitAmount;
    private Double monthlyLimitAmount;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "card")
    List<Transaction> transactions;
}
