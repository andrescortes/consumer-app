package co.com.consumer.consumer.character;

import co.com.consumer.model.character.Character;
import co.com.consumer.model.character.Information;
import co.com.consumer.model.character.ResultCharacter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

class CharacterRestConsumerTest {

    private static CharacterRestConsumer characterRestConsumer;
    private static MockWebServer mockWebServer;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        ModelMapper mapper = new ModelMapper();
        mockWebServer.start();
        WebClient webClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();
        characterRestConsumer = new CharacterRestConsumer(webClient, mapper);
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Validate the function getAllCharacters")
    void allCharacters() {
        Character character = Character.builder()
                .info(Information.builder()
                        .count(1L)
                        .pages(1L)
                        .build())
                .results(List.of(ResultCharacter.builder()
                        .id(1L)
                        .name("Test")
                        .species("Alive")
                        .url("https://test.com")
                        .build()))
                .build();
        try {
            String body = objectMapper.writeValueAsString(character);
            mockWebServer.enqueue(new MockResponse()
                    .setHeader("Content-Type", "application/json")
                    .setResponseCode(200)
                    .setBody(body)
            );
            StepVerifier.create(characterRestConsumer.allCharacters())
                    .consumeNextWith(chter -> {
                        Assertions.assertEquals(1L, chter.getInfo().getCount());
                        Assertions.assertEquals(1L, chter.getInfo().getPages());
                        Assertions.assertEquals("Test", chter.getResults().get(0).getName());
                    })
                    .verifyComplete();
        } catch (JsonProcessingException e) {
            Assertions.fail(e);
        }
    }

    @Test
    @DisplayName("Validate the function getAllCharacters will failed")
    void allCharactersFail() {


        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setResponseCode(500)
                .setBody("{\"error\":\"internal server error\"}")
        );
        StepVerifier.create(characterRestConsumer.allCharacters())
                .consumeErrorWith(thr -> Assertions.assertTrue(thr.getMessage().contains("Server error")))
                .verify();
    }
}