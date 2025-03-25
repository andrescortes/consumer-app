package co.com.consumer.api.security.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
