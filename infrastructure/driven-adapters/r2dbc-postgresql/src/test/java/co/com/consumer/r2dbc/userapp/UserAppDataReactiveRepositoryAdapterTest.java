package co.com.consumer.r2dbc.userapp;

import co.com.consumer.model.userapp.Role;
import co.com.consumer.model.userapp.UserApp;
import co.com.consumer.r2dbc.userapp.dto.RoleData;
import co.com.consumer.r2dbc.userapp.dto.UserAppData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class UserAppDataReactiveRepositoryAdapterTest {

    @InjectMocks
    UserAppDataReactiveRepositoryAdapter adapter;

    @Mock
    UserAppDataReactiveRepository repository;

    @Mock
    ObjectMapper objectMapper;

    UserAppData userAppData;
    UserApp userApp;

    @BeforeEach
    void setUp() {
        userAppData = UserAppData.builder()
                .id(1L)
                .username("test")
                .password("test")
                .roles(Collections.singletonList(RoleData.ROLE_USER))
                .build();
        userApp = UserApp.builder()
                .id(1L)
                .username("test")
                .password("test")
                .roles(Collections.singletonList(Role.ROLE_USER))
                .build();

    }

    @Test
    void byUsername() {
        Mockito.when(repository.findUserAppDataByUsername(anyString())).thenReturn(Mono.just(userAppData));
        Mockito.when(objectMapper.mapBuilder(any(), any())).thenReturn(userApp.toBuilder());

        adapter.byUsername("username")
                .as(StepVerifier::create)
                .expectNextMatches(user-> user.getUsername().equals("test")
                && user.getPassword().equals("test"))
                .verifyComplete();
    }

    @Test
    void byUsernameNotFound() {
        Mockito.when(repository.findUserAppDataByUsername(anyString())).thenReturn(Mono.empty());

        adapter.byUsername("pedro")
                .as(StepVerifier::create)
                .expectErrorMessage("Username pedro not found")
                .verify();
    }

    @Test
    void createUser() {
        Mockito.when(repository.save(any())).thenReturn(Mono.just(userAppData));
        Mockito.when(objectMapper.mapBuilder(any(), any())).thenReturn(userApp.toBuilder());

        adapter.createUser(userApp)
                .as(StepVerifier::create)
                .expectNextMatches(user -> user.getUsername().equals("test"))
                .verifyComplete();
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
//        Mockito.when(repository.findUserAppDataByUsername(anyString())).thenReturn(Mono.just(userAppData));
//        Mockito.when(objectMapper.mapBuilder(any(), any())).thenReturn(userApp.toBuilder());
//        Mockito.when(objectMapper.mapBuilder(any(), any())).thenReturn(userAppData.toBuilder());
//        Mockito.when(repository.delete(any())).thenReturn(Mono.empty());
//
//        adapter.deleteUser("username")
//                .as(StepVerifier::create)
//                .verifyComplete();
    }
}