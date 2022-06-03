package com.bstar.banking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;
    private Double amount;
    private String pinCode;
    private String body;
    private String unitCurrency;
    private Boolean status;
    private Integer transactionType;
    private String beneficiaryAccountNumber;
    private String beneficiaryName;
    private String beneficiaryEmail;
    private String beneficiaryPhone;
    private String createPerson;
    private Date createDate;
    private Double balance;
    @ManyToOne
    @JoinColumn(name = "account_number")
    Account account;

}
