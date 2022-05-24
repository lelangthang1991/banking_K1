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
    private String status;
    private String message;
    private String accessToken;
    private Date accessTokenExpire;
    private String refreshToken;
    private Date refreshTokenExpire;
    public LoginResponsePayload(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
