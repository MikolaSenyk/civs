/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.model.dto;

import java.util.Date;
import net.sf.json.JSONObject;
import ua.poltava.senyk.civs.utils.DatetimeFormat;

/**
 * Assistance DTO
 * @author mikola
 */
public class AssistanceDto extends MessageDto {
	
	// fields
	private long id;
	private Date createTime;
    private UserDto user;
    private AssistanceGroupDto group;
	private String description;
    private boolean approved;

	public AssistanceDto() {
		this.id = 0L;
	}

	@Override
	public JSONObject getJSON() {
		JSONObject json = super.getJSON();
		json.put("id", getId());
        json.put("createTime", DatetimeFormat.getDate(getCreateTime()));
		json.put("userId", getUser().getId());
		json.put("groupId", getGroup().getId());
		json.put("description", getDescription());
		json.put("approved", isApproved());
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

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public AssistanceGroupDto getGroup() {
		return group;
	}

	public void setGroup(AssistanceGroupDto group) {
		this.group = group;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
}
