package co.com.consumer.config;

import co.com.consumer.model.character.Character;
import co.com.consumer.model.character.gateways.CharacterRepository;
import co.com.consumer.model.userapp.UserApp;
import co.com.consumer.model.userapp.gateways.UserAppRepository;
import co.com.consumer.usecase.characterlist.CharacterListUseCase;
import co.com.consumer.usecase.userapp.UserAppUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {
        // usecases
        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }

        @Bean
        public CharacterListUseCase characterListUseCase(CharacterRepository characterRepository) {
            return new CharacterListUseCase(characterRepository);
        }

        @Bean
        public UserAppUseCase userAppUseCase(UserAppRepository userAppRepository) {
            return new UserAppUseCase(userAppRepository);
        }

        // repositories
        @Bean
        public CharacterRepository characterRepository() {
            return new CharacterRepository() {
                @Override
                public Mono<Character> allCharacters() {
                    return Mono.empty();
                }
            };
        }

        @Bean
        public UserAppRepository userAppRepository() {
            return new UserAppRepository() {
                @Override
                public Mono<UserApp> byUsername(String username) {
                    return Mono.empty();
                }

                @Override
                public Mono<UserApp> createUser(UserApp userApp) {
                    return Mono.empty();
                }

                @Override
                public Mono<UserApp> updateUser(String username, UserApp userApp) {
                    return Mono.empty();
                }

                @Override
                public Mono<Void> deleteUser(String username) {
                    return Mono.empty();
                }
            };
        }
    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}