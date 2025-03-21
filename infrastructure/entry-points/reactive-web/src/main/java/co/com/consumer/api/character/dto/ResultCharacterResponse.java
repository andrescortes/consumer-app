package co.com.consumer.api.character.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
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
