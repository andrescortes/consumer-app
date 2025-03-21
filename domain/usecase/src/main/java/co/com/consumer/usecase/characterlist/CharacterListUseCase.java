package co.com.consumer.usecase.characterlist;

import co.com.consumer.model.character.Character;
import co.com.consumer.model.character.gateways.CharacterRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CharacterListUseCase {

    private final CharacterRepository characterRepository;

    public Mono<Character> characters(){
        return characterRepository.allCharacters();
    }
}
