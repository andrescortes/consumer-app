package co.com.consumer.api.security;

import co.com.consumer.api.security.dto.AuthPermissionRequest;
import co.com.consumer.api.security.dto.LoginRequest;
import co.com.consumer.api.security.dto.RefreshTokenRequest;
import co.com.consumer.api.security.dto.TokenResponse;
import co.com.consumer.api.security.dto.UserAppDto;
import co.com.consumer.api.security.jwt.JWTUtil;
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

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthHandler {

    private final JWTUtil jwtUtil;
    private final PBKDF2Encoder passwordEncoder;
    private final UserAppUseCase userAppUseCase;
    private final UserAppMapper userAppMapper;

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(this::processRequestLogin)
                .flatMap(tokenResponse -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(tokenResponse)
                );
    }

    private Mono<TokenResponse> processRequestLogin(LoginRequest auth) {
        return userAppUseCase
                .getUserApp(auth.getUsername())
                .map(userAppMapper::toDetails)
                .filter(userDetails -> passwordEncoder.encode(auth.getPassword()).equals(userDetails.getPassword()))
                .map(userApp -> {
                    UserApp user = userAppMapper.toEntity(userApp);
                    return new TokenResponse(jwtUtil.generateAccessToken(user), jwtUtil.generateRefreshToken(user));
                })
                .switchIfEmpty(Mono.defer(() -> Mono.error(new IllegalArgumentException("Invalid username or password"))));
    }

    public Mono<ServerResponse> signup(ServerRequest request) {
        log.info("Signup request received");
        return request
                .bodyToMono(UserAppDto.class)
                .flatMap(this::processRequestSignup)
                .flatMap(message -> ServerResponse
                        .ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .bodyValue(message)
                );
    }

    private Mono<String> processRequestSignup(UserAppDto dto) {
        UserApp user = UserApp.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .enabled(dto.getEnabled())
                .roles(dto.getRoles().stream().map(Role::valueOf).collect(Collectors.toSet()))
                .build();
        return userAppUseCase
                .saveUserApp(user)
                .then(Mono.just("User signed up successfully"));
    }

    public Mono<ServerResponse> addPermission(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AuthPermissionRequest.class)
                .flatMap(authPermissionRequest -> processRequestAddOrRemovePermission(authPermissionRequest, true))
                .flatMap(message -> ServerResponse
                        .ok()
                        .bodyValue(message)
                );
    }

    private Mono<String> processRequestAddOrRemovePermission(AuthPermissionRequest permission, boolean addPermission) {
        return userAppUseCase.getUserApp(permission.getUsername())
                .map(userAppMapper::toDetails)
                .flatMap(userAppData -> {
                    if (permission.getPermissions().isEmpty()) {
                        return Mono.just("There are no permissions to perform this action");
                    }
                    Set<String> rolesAllowed = Arrays.stream(Role.values())
                            .map(Enum::name)
                            .filter(role -> permission.getPermissions().contains(role))
                            .collect(Collectors.toSet());
                    if (rolesAllowed.isEmpty()) {
                        return Mono.just("Roles not allowed to perform this action");
                    }
                    Set<Role> roles = rolesAllowed
                            .stream()
                            .map(Role::valueOf)
                            .collect(Collectors.toSet());
                    log.info("Adding permissions: {}", roles);
                    if (addPermission) {
                        userAppData.getRoles().addAll(roles);
                    } else {
                        userAppData.getRoles().removeAll(roles);
                    }
                    return userAppUseCase.updateUserApp(userAppData.getUsername(), userAppMapper.toEntity(userAppData))
                            .then(Mono.just("Operation completed successfully"));
                });
    }


    public Mono<ServerResponse> deletePermission(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AuthPermissionRequest.class)
                .flatMap(request -> processRequestAddOrRemovePermission(request, false))
                .flatMap(message -> ServerResponse
                        .ok()
                        .bodyValue(message)
                );
    }

    public Mono<ServerResponse> refreshToken(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RefreshTokenRequest.class)
                .flatMap(request -> processRefreshToken(request)
                        .flatMap(accessToken -> ServerResponse
                                .ok()
                                .bodyValue(new TokenResponse(accessToken, request.getRefreshToken()))
                        ));
    }

    private Mono<String> processRefreshToken(RefreshTokenRequest request) {
        return Mono.fromCallable(() -> jwtUtil.extractUsername(request.getRefreshToken()))
                .flatMap(username -> userAppUseCase
                        .getUserApp(username)
                        .map(userAppMapper::toDetails))
                .map(userDetails -> jwtUtil.generateAccessToken(userAppMapper.toEntity(userDetails)));
    }
}
