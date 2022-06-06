package com.bstar.banking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Transaction {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String transactionId;
    private Double amount;
    private String pinCode;
    private String body;
    private String unitCurrency;
    private Boolean status;
    private Integer transactionType;
    private String beneficiaryCardNumber;
    private String beneficiaryName;
    private String beneficiaryEmail;
    private String beneficiaryPhone;
    private String createPerson;
    private Date createDate;
    private Double balance;
    private Double fee;
    @ManyToOne
    @JoinColumn(name = "card_number")
    Card card;

}
