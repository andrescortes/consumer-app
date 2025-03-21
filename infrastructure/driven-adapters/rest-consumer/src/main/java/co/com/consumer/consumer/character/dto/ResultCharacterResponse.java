package co.com.consumer.consumer.character.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ResultCharacterResponse {

    private Long id;
    private String name;
    private String status;
    private String species;
    private String image;
    private String url;
    private LocalDateTime created;
}
