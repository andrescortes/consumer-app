package co.com.consumer.api.security.conf;

import co.com.consumer.api.security.Jwt.JWTAuthenticationManager;
import co.com.consumer.model.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final JWTAuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        log.info("SecurityContextRepository save called");
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith(AppConstants.TOKEN_PREFIX))
                .flatMap(authHeader -> {
                    log.info("Getting token from header");
                    String token = authHeader.substring(AppConstants.TOKEN_PREFIX.length());
                    Authentication auth = new UsernamePasswordAuthenticationToken(token, token, null);
                    return authenticationManager
                            .authenticate(auth)
                            .map(SecurityContextImpl::new);
                });
    }
}
