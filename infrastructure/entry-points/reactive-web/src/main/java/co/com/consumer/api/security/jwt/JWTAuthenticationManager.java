package co.com.consumer.api.security.jwt;

import co.com.consumer.model.utils.AppConstants;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTUtil jwtUtil;

    @SuppressWarnings("unchecked")
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String username = jwtUtil.extractUsername(token);
        return Mono.fromCallable(() -> jwtUtil.validateToken(token, username))
                .filter(valid -> valid)
                .switchIfEmpty(Mono.defer(Mono::empty))
                .map(valid -> {
                    Claims allClaimsFromToken = jwtUtil.getAllClaimsFromToken(token);
                    List<String> roles = allClaimsFromToken.get(AppConstants.ROLES, List.class);
                    return new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            roles.stream().map(SimpleGrantedAuthority::new).toList()
                    );
                });
    }
}
