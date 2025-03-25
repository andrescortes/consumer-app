package co.com.consumer.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static co.com.consumer.api.commons.Constants.AUTH_REFRESH_TOKEN;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static co.com.consumer.api.commons.Constants.AUTH_ADD_PERMISSIONS;
import static co.com.consumer.api.commons.Constants.AUTH_LOGIN;
import static co.com.consumer.api.commons.Constants.AUTH_REMOVE_PERMISSIONS;
import static co.com.consumer.api.commons.Constants.AUTH_SIGNUP;

@Configuration
public class AuthRouter {

    @Bean
    public RouterFunction<ServerResponse> authenticatedRoutes(AuthHandler authHandler) {
        return route(POST(AUTH_LOGIN), authHandler::login)
                .andRoute(POST(AUTH_SIGNUP), authHandler::signup)
                .andRoute(POST(AUTH_ADD_PERMISSIONS), authHandler::addPermission)
                .andRoute(POST(AUTH_REMOVE_PERMISSIONS), authHandler::deletePermission)
                .andRoute(POST(AUTH_REFRESH_TOKEN), authHandler::refreshToken);
    }
}
