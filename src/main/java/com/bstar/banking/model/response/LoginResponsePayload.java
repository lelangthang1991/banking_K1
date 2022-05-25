package com.bstar.banking.model.response;

import com.bstar.banking.model.request.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponsePayload implements Serializable {
    private String statusCode;
    private String statusDescription;
    private String accessToken;
    private Long accessTokenExpire;
    private String refreshToken;
    private Long refreshTokenExpire;
    public LoginResponsePayload(String status, String message) {
        this.statusCode = status;
        this.statusDescription = message;
    }
}
