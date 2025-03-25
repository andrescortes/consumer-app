package co.com.consumer.api.security.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthPermissionRequest {
    private String username;
    private List<String> permissions;
}
