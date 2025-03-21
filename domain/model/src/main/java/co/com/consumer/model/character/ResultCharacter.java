package co.com.consumer.model.character;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ResultCharacter {

    private Long id;
    private String name;
    private String status;
    private String species;
    private String image;
    private String url;
    private LocalDateTime created;
}
