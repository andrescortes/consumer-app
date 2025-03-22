package co.com.consumer.api.security.conf;

import co.com.consumer.api.security.Jwt.JWTAuthenticationManager;
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
                                        .pathMatchers(HttpMethod.POST, "/auth/signup", "/auth/login").permitAll()
                                        .pathMatchers(HttpMethod.GET, "/characters").hasAnyRole("USER")
                                        .anyExchange()
                                        .authenticated()
                        )
                        .build();
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
        return new DefaultMethodSecurityExpressionHandler();
    }
}
