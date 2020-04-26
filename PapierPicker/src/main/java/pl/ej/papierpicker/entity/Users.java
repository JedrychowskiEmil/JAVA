package pl.ej.papierpicker.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="users")
public class Users {
	
	@OneToMany(fetch = FetchType.EAGER ,cascade = CascadeType.ALL)
	@JoinColumn(name="username")
	private List<Authorities> authorities;
	
	@NotNull(message="is required")
	@Size(min=1, message="is required")
	@Id
	@Column(name="username")
	private String username;
	
	@NotNull(message="is required")
	@Size(min=1, message="is required")
	@Column(name="password")
	private String password;
	
	@Column(name="enabled")
	private int enabled;
	
	public List<Authorities> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authorities> authorities) {
		this.authorities = authorities;
	}
	

	public Users() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "Users [username=" + username + ", password=" + password + ", enabled=" + enabled + "]";
	}
	
	
}
