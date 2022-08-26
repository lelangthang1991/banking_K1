package com.bstar.banking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class Card {
    @Id
    private String cardNumber;
    private Double balance;
    private String pinCode;
    private Integer cardType;
    private Boolean isActivated;
    @CreatedBy
    private String createPerson;
    @LastModifiedBy
    private String updatePerson;
    @LastModifiedDate
    private Date updateDate;
    @CreatedDate
    private Date createDate;
    private Integer level;
    private Double dailyLimitAmount;
    private Double monthlyLimitAmount;
    private Double dailyAvailableTransfer;
    private Double monthlyAvailableTransfer;

    @ManyToOne()
    @JoinColumn(name = "user_email")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "card")
    List<Transaction> transactions;
}
