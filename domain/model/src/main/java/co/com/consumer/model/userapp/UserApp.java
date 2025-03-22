package co.com.consumer.model.userapp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserApp {

    private Long id;
    private String username;
    private String password;
    private Boolean enabled;
    private List<Role> roles;
}
