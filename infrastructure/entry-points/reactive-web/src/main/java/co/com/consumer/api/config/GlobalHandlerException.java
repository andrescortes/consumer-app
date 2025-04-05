package co.com.consumer.api.config;

import co.com.consumer.api.commons.dto.ApiErrorResponse;
import co.com.consumer.model.exceptions.InvalidCredential;
import co.com.consumer.model.exceptions.JWTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(JWTException.class)
    public ResponseEntity<ApiErrorResponse> handleJWTException(JWTException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiErrorResponse
                        .builder()
                        .message(ex.getMessage())
                        .code(ex.getCode())
                        .traces(getClassesName(ex.getStackTrace()))
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(InvalidCredential.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredential(InvalidCredential ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ApiErrorResponse.builder()
                                .message(ex.getMessage())
                                .code(ex.getCode())
                                .traces(getClassesName(ex.getStackTrace()))
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiErrorResponse
                        .builder()
                        .message(ex.getMessage())
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .traces(getClassesName(ex.getStackTrace()))
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    private List<String> getClassesName(StackTraceElement[] stackTrace) {
        return Arrays.stream(stackTrace)
                .map(StackTraceElement::getClassName)
                .map(name -> name.substring(name.lastIndexOf(".") + 1))
                .limit(2)
                .toList();
    }
}
