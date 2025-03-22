package co.com.consumer.model.userapp.gateways;

import co.com.consumer.model.userapp.UserApp;
import reactor.core.publisher.Mono;

public interface UserAppRepository {
    Mono<UserApp> byUsername(String username);

    Mono<UserApp> createUser(UserApp userApp);

    Mono<UserApp> updateUser(String username, UserApp userApp);

    Mono<Void> deleteUser(String username);
}
