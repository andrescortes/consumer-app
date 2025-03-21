package co.com.consumer.api.character;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CharacterRouter {

    @Bean
    public RouterFunction<ServerResponse> routerFunctionCharacter(CharacterHandler characterHandler) {
        return route(GET("/characters"), characterHandler::getCharacters);
    }
}
