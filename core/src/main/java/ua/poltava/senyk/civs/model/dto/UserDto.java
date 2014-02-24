/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.model.dto;

import java.util.Date;
import net.sf.json.JSONObject;
import ua.poltava.senyk.civs.model.UserRole;
import ua.poltava.senyk.civs.utils.DatetimeFormat;

/**
 * Data Transfer Object of User entity
 * @author mikola
 */
public class UserDto extends MessageDto {
	
	private long id;
	private Date createTime;
    private String login;
    private String passwd;
	private UserRole role;
	private boolean enabled;

	public UserDto() {
		super();
		this.id = 0L;
		this.createTime = new Date(); // FIXME use GMT+0
	}

	@Override
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		json.put("id", this.getId());
		json.put("login", this.getLogin());
		json.put("role", this.getRole());
		json.put("enabled", this.isEnabled());
		json.put("createTime", DatetimeFormat.getDate(this.createTime));
		return json;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
