package co.com.consumer.model.exceptions;

import lombok.Getter;

@Getter
public class JWTException extends RuntimeException {
    private final int code;
    public JWTException(String message, int code) {
        super(message);
        this.code = code;
    }
}
