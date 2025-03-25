package co.com.consumer.api.config;

import co.com.consumer.model.exceptions.JWTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        ErrorAttributeOptions errorAttributeOptions = ErrorAttributeOptions.of(ErrorAttributeOptions.Include.values());
        Map<String, Object> errorAttributes = getErrorAttributes(request, errorAttributeOptions);
        Throwable error = getError(request);
        final String message;
        if (error instanceof UnsupportedMediaTypeStatusException exception) {
            message = getUnsupportedMediaTypeMessage(exception);
        } else {
            message = (String) errorAttributes.get("message");
        }
        assert message != null;
        int statusCode = Integer.parseInt(Objects.toString(errorAttributes.get("status")));
        if(error instanceof JWTException exception) {
            statusCode = exception.getCode();
        }
        HttpStatus status = HttpStatus.resolve(statusCode);
        assert status != null;
        String[] exceptions = errorAttributes.get("exception").toString().split("\\.");
        Map<String, Object> response = Map.of(
                "status", status.value(),
                "timestamp", LocalDateTime.now(),
                "message", message,
                "errors", List.of(
                        errorAttributes.get("error").toString(),
                        exceptions[exceptions.length - 1]
                ));
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(BodyInserters.fromValue(response));
    }

    private String getUnsupportedMediaTypeMessage(UnsupportedMediaTypeStatusException ex) {
        return ex.getMessage();
    }
}
