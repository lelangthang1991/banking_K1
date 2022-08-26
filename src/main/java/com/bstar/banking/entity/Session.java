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
public class Session {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String sessionId;
    private String deviceType;
    private String ipAddress;
    private String refreshToken;
    private Date expired;
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "user_email")
    User user;
}
