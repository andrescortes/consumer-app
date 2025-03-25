package co.com.consumer.api.commons;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String AUTH_LOGIN = "/auth/login";
    public static final String AUTH_SIGNUP = "/auth/signup";
    public static final String AUTH_ADD_PERMISSIONS = "/auth/add-permissions";
    public static final String AUTH_REMOVE_PERMISSIONS = "/auth/remove-permissions";
    public static final String AUTH_REFRESH_TOKEN = "/auth/refresh-token";
    public static final String CHARACTERS = "/characters";
}
