package space.nixus.pubtrans.model;

import org.springframework.security.core.GrantedAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Authority implements GrantedAuthority {

    private String role;

    @Override
    public String getAuthority() {
        return "ROLE_" + role;
    }
}
