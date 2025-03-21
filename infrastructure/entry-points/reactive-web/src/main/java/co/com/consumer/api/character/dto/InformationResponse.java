package co.com.consumer.api.character.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InformationResponse {

    private Long count;
    private Long pages;
    private String next;
    private String prev;
}
