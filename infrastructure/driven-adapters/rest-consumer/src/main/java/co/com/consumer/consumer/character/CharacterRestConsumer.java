package co.com.consumer.consumer.character;

import co.com.consumer.consumer.character.dto.CharacterResponse;
import co.com.consumer.model.character.Character;
import co.com.consumer.model.character.gateways.CharacterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterRestConsumer implements CharacterRepository {

    private final WebClient client;
    private final ModelMapper modelMapper;

    @Override
    public Mono<Character> allCharacters() {
        return client
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment("/character").build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new IllegalAccessException("Client error in -> ".concat(clientResponse.logPrefix()))))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new IllegalAccessException("Server error in -> ".concat(clientResponse.logPrefix()))))
                .bodyToMono(CharacterResponse.class)
                .map(this::toEntity);
    }

    private Character toEntity(CharacterResponse characterResponse) {
        return modelMapper.map(characterResponse, Character.class);
    }
}
