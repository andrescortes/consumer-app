package co.com.consumer.api.character;

import co.com.consumer.api.character.dto.CharacterResponse;
import co.com.consumer.model.character.Character;
import co.com.consumer.usecase.characterlist.CharacterListUseCase;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CharacterHandler {

    private final ModelMapper modelMapper;
    private final CharacterListUseCase characterListUseCase;

    public Mono<ServerResponse> getCharacters(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        characterListUseCase.characters().map(this::toDto),
                        CharacterResponse.class
                );
    }

    private CharacterResponse toDto(Character character) {
        return modelMapper.map(character, CharacterResponse.class);
    }
}
