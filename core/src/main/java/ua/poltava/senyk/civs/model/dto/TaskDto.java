/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.model.dto;

import java.util.Date;
import net.sf.json.JSONObject;
import ua.poltava.senyk.civs.model.TaskStatus;
import ua.poltava.senyk.civs.utils.DatetimeFormat;

/**
 * Data Transfer Object for Task entity
 * @author mikola
 */
public class TaskDto extends MessageDto {
	
	private long id;
    private UserDto user;
    private Date createTime;
	private int priority;
    private String title;
    private String description;
    private TaskStatus status;

	public TaskDto() {
		super();
		this.id = 0L;
	}

	@Override
	public JSONObject getJSON() {
		JSONObject json = super.getJSON();
		json.put("id", getId());
		json.put("userId", getUser().getId());
		json.put("priority", getPriority());
		json.put("createTime", DatetimeFormat.getDateTime(getCreateTime()));
		json.put("title", getTitle());
		json.put("description", getDescription());
		json.put("status", getStatus().name());
		return json;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}
		
}
