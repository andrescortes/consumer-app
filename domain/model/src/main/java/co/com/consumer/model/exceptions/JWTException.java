package co.com.consumer.model.exceptions;

public class JWTException extends RuntimeException {
    public JWTException(String message) {
        super(message);
    }
}
