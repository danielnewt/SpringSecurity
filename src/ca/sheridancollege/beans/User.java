package ca.sheridancollege.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "users")
public class User implements Serializable{
	
	private static final long serialVersionUID = 2531019282759999971L;
	
	@Id
	@Column(name = "username", unique = true, nullable = false, length = 45)
	private String username;
	@Column(name = "password", nullable = false, length = 60)
	private String password;
	@Column(name = "enabled", nullable = false)
	private boolean enabled;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	@Cascade({CascadeType.ALL})
	private Set<UserRole> userRole = new HashSet<UserRole>(0);
	
	public User(){
	}
	
	public User(String username, String password, boolean enabled, boolean admin){
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		if(admin){
			userRole.add(new UserRole(this, UserRole.ADMIN_ROLE));
		}else{
			userRole.add(new UserRole(this, UserRole.USER_ROLE));
		}
	}
	
	public User(String username, String password, boolean enabled, Set<UserRole> userRole){
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.userRole = userRole;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public Set<UserRole> getUserRole() {
		return userRole;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setUserRole(Set<UserRole> userRole) {
		this.userRole = userRole;
	}
	
}
