package co.com.consumer.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouter {

    @Bean
    public RouterFunction<ServerResponse> authenticatedRoutes(AuthHandler authHandler) {
        return route(POST("/auth/login"), authHandler::login)
                .andRoute(POST("/auth/signup"), authHandler::signup);
    }
}
