package co.com.consumer.api.security.conf;

import co.com.consumer.api.security.jwt.JWTAuthenticationManager;
import co.com.consumer.model.userapp.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import static co.com.consumer.api.commons.Constants.AUTH_ADD_PERMISSIONS;
import static co.com.consumer.api.commons.Constants.AUTH_LOGIN;
import static co.com.consumer.api.commons.Constants.AUTH_REMOVE_PERMISSIONS;
import static co.com.consumer.api.commons.Constants.AUTH_SIGNUP;
import static co.com.consumer.api.commons.Constants.CHARACTERS;

@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity()
@Configuration
public class WebSecurityConf {

    private final JWTAuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return
                http
                        .exceptionHandling(exceptionHandlingSpec ->
                                exceptionHandlingSpec
                                        .authenticationEntryPoint((exchange, denied) ->
                                                Mono.fromRunnable(() -> exchange
                                                        .getResponse()
                                                        .setStatusCode(HttpStatus.UNAUTHORIZED))
                                        )
                                        .accessDeniedHandler((exchange, denied) ->
                                                Mono.fromRunnable(() -> exchange
                                                        .getResponse()
                                                        .setStatusCode(HttpStatus.FORBIDDEN))
                                        )
                        )
                        .csrf(ServerHttpSecurity.CsrfSpec::disable)
                        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                        .logout(ServerHttpSecurity.LogoutSpec::disable)
                        .authenticationManager(authenticationManager)
                        .securityContextRepository(securityContextRepository)
                        .authorizeExchange(authorizeExchangeSpec ->
                                authorizeExchangeSpec
                                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                        .pathMatchers(HttpMethod.POST, AUTH_SIGNUP, AUTH_LOGIN).permitAll()
                                        .pathMatchers(HttpMethod.POST, AUTH_ADD_PERMISSIONS, AUTH_REMOVE_PERMISSIONS).hasRole(Role.ROLE_ADMIN.name().substring(5))
                                        .anyExchange()
                                        .authenticated()
                        )
                        .build();
    }
}
