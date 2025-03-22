package co.com.consumer.usecase.userapp;

import co.com.consumer.model.userapp.UserApp;
import co.com.consumer.model.userapp.gateways.UserAppRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserAppUseCase {
    private final UserAppRepository userAppRepository;

    public Mono<UserApp> getUserApp(String username) {
        return userAppRepository.byUsername(username);
    }

    public Mono<UserApp> saveUserApp(UserApp userApp) {
        return userAppRepository.createUser(userApp);
    }

    public Mono<UserApp> updateUserApp(String username, UserApp userApp) {
        return userAppRepository.updateUser(username, userApp);
    }

    public Mono<Void> deleteUserApp(String username) {
        return userAppRepository.deleteUser(username);
    }
}
