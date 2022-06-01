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
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sessionId;
    private String deviceType;
    private String ipAddress;
    private String refreshToken;
    private Date expired;
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "user_email")
    User user;
}
