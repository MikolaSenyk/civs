/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.model.dto;

import net.sf.json.JSONObject;

/**
 * Data Transfer Object for AssistanceGroup
 * @author mikola
 */
public class AssistanceGroupDto extends MessageDto {
	
	// fields
	private long id;
	private String name;
	private boolean readOnly;

	public AssistanceGroupDto() {
		this.id = 0L;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	public JSONObject getJSON() {
		JSONObject json = super.getJSON();
		json.put("id", getId());
		json.put("name", getName());
		json.put("readOnly", isReadOnly());
		return json;
	}
	
}
