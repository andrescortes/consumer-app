package co.com.consumer.r2dbc.userapp;

import co.com.consumer.model.userapp.UserApp;
import co.com.consumer.model.userapp.gateways.UserAppRepository;
import co.com.consumer.r2dbc.helper.ReactiveAdapterOperations;
import co.com.consumer.r2dbc.userapp.dto.UserAppData;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserAppDataReactiveRepositoryAdapter extends ReactiveAdapterOperations<UserApp, UserAppData, Long, UserAppDataReactiveRepository> implements UserAppRepository {

    protected UserAppDataReactiveRepositoryAdapter(UserAppDataReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, UserApp.UserAppBuilder.class).build());
    }

    @Override
    public Mono<UserApp> byUsername(String username) {
        return repository.findUserAppDataByUsername(username)
                .map(super::toEntity)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new IllegalArgumentException("Username " + username + " not found"))));
    }

    @Override
    public Mono<UserApp> createUser(UserApp userApp) {
        return repository.save(super.toData(userApp))
                .map(super::toEntity);
    }

    @Override
    public Mono<UserApp> updateUser(String username, UserApp userApp) {
        return byUsername(username)
                .map(user ->
                        user.toBuilder()
                                .username(username)
                                .password(userApp.getPassword())
                                .build()
                ).flatMap(user -> repository.save(super.toData(user)))
                .map(super::toEntity);
    }

    @Override
    public Mono<Void> deleteUser(String username) {
        return byUsername(username)
                .flatMap(user -> repository.deleteById(user.getId()));
    }
}
