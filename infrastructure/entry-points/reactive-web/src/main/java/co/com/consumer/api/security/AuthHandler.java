package co.com.consumer.api.security;

import co.com.consumer.api.security.Jwt.JWTUtil;
import co.com.consumer.api.security.dto.AuthRequest;
import co.com.consumer.api.security.dto.AuthResponse;
import co.com.consumer.api.security.dto.UserAppDto;
import co.com.consumer.api.security.mapper.UserAppMapper;
import co.com.consumer.api.security.utils.PBKDF2Encoder;
import co.com.consumer.model.userapp.Role;
import co.com.consumer.model.userapp.UserApp;
import co.com.consumer.usecase.userapp.UserAppUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthHandler {

    private final JWTUtil jwtUtil;
    private final PBKDF2Encoder passwordEncoder;
    private final UserAppUseCase userAppUseCase;
    private final UserAppMapper userAppMapper;

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(AuthRequest.class)
                .flatMap(this::processRequestLogin)
                .flatMap(authResponse -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(authResponse)
                );
    }

    private Mono<AuthResponse> processRequestLogin(AuthRequest auth) {
        return userAppUseCase
                .getUserApp(auth.getUsername())
                .map(userAppMapper::toData)
                .filter(userDetails -> {
                    log.info("UserDetails: {}", userDetails);
                    return passwordEncoder.encode(auth.getPassword()).equals(userDetails.getPassword());
                })
                .map(userApp -> new AuthResponse(jwtUtil.generateToken(userAppMapper.toEntity(userApp))))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new IllegalArgumentException("Invalid username or password"))));
    }

    public Mono<ServerResponse> signup(ServerRequest request) {
        log.info("Signup request received");
        return request
                .bodyToMono(UserAppDto.class)
                .doOnNext(userApp -> log.info("Signup user app: {}", userApp))
                .flatMap(this::processRequestSignup)
                .flatMap(message -> ServerResponse
                        .ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .bodyValue(message)
                );
    }

    private Mono<String> processRequestSignup(UserAppDto dto) {
        log.info("processRequestSignup request received: {}", dto);
        UserApp user = UserApp.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .enabled(dto.getEnabled())
                .roles(dto.getRoles().stream().map(Role::valueOf).toList())
                .build();
        return userAppUseCase
                .saveUserApp(user)
                .then(Mono.just("User signed up successfully"));
    }
}
