package space.nixus.auth.model;

import java.util.Collection;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.google.cloud.spring.data.datastore.core.mapping.Entity;

@Entity(name = "user")
public class User extends UserParams implements UserDetails {

	@Id
	private Long id;

	@Transient
	private Collection<Authority> authorities;

	public User() {
		super();
		this.id = null;
		authorities = null;
	}

	public User(Long id, String email, String password, String role, boolean enabled) {
		super(email, password, role, enabled);
		this.id = id;
	}

	public long getId() {
		return this.id;
	}

	public User update(UserParams params) {
		if(params.getEmail() != null) {
			setEmail(params.getEmail());
		}
		if(params.getPassword() != null) {
			setPassword(params.getPassword());
		}
		if(params.getRole() != null) {
			setRole(params.getRole());
		}
		if(params._getEnabled() != null) {
			setEnabled(params.getEnabled());
		}
		return this;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(authorities == null) {
			if(getRole() == null) {
				authorities = List.of();
			} else {
				authorities = List.of(new Authority(getRole()));
			}
		}
		return authorities;
	}

	@Override
	public void setRole(String role) {
		authorities = null;
		super.setRole(role);
	}

	@Override
	public String getUsername() {
		return getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return getEnabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return getEnabled();
	}

	@Override
	public boolean isEnabled() {
		return getEnabled();
	}
}
