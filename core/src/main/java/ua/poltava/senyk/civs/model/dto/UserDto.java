/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.model.dto;

import net.sf.json.JSONObject;
import ua.poltava.senyk.civs.model.UserRole;

/**
 * Data Transfer Object of User entity
 * @author mikola
 */
public class UserDto extends MessageDto {
	
	private long id;
    private String login;
    private String passwd;
	private UserRole role;
	private boolean enabled;

	public UserDto() {
		super();
		this.id = 0L;
	}

	@Override
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		json.put("id", this.getId());
		json.put("login", this.getLogin());
		json.put("role", this.getRole());
		json.put("enabled", this.isEnabled());
		return json;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
