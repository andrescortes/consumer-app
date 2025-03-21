package co.com.consumer.model.character.gateways;

import co.com.consumer.model.character.Character;
import reactor.core.publisher.Mono;

public interface CharacterRepository {
    Mono<Character> allCharacters();
}
