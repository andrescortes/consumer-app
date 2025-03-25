package co.com.consumer.api.security.dto;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
