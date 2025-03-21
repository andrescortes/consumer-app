package co.com.consumer.consumer.character.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
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
