package co.com.consumer.r2dbc.userapp.dto;

import co.com.consumer.model.userapp.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "user_app_data")
public class UserAppData implements Persistable<Long> {

    @Id
    private Long id;
    private String username;
    private String password;
    private Boolean enabled;
    private Set<Role> roles;

    @Override
    public boolean isNew() {
        return Objects.isNull(id);
    }
}
