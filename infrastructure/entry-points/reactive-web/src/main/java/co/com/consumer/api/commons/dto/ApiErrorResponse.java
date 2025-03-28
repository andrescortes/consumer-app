package co.com.consumer.api.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ApiErrorResponse {
    private String message;
    private int code;
    private List<String> traces;
    private LocalDateTime timestamp;
}
