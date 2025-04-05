package co.com.consumer.model.exceptions;

import lombok.Getter;

@Getter
public class InvalidCredential extends RuntimeException {
    private final int code;
    public InvalidCredential(String message, int code) {
        super(message);
        this.code = code;
    }
}
