package co.com.consumer.r2dbc.userapp;

import co.com.consumer.r2dbc.userapp.dto.UserAppData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserAppDataReactiveRepository extends ReactiveCrudRepository<UserAppData, Long>, ReactiveQueryByExampleExecutor<UserAppData> {
    Mono<UserAppData> findUserAppDataByUsername(String username);
}
