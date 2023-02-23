package space.nixus.auth.model;

import java.io.Serializable;

public class UserParams implements Serializable {
    private String email;
    private String password;
	private String role;
	private Boolean enabled;

    public UserParams() {
        this.email = null;
        this.password = null;
        this.role = null;
        this.enabled = null;
    }
    
    public UserParams(String email, String password, String role, Boolean enabled) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    protected void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean getEnabled() {
        return enabled;
    }

    protected Boolean _getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
