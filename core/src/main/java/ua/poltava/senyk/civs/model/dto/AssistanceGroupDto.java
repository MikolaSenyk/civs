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
	private int level;
    private AssistanceGroupDto parentGroup;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public AssistanceGroupDto getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(AssistanceGroupDto parentGroup) {
        this.parentGroup = parentGroup;
    }

	@Override
	public JSONObject getJSON() {
		JSONObject json = super.getJSON();
		json.put("id", getId());
		json.put("name", getName());
		json.put("level", getLevel());
        json.put("parentId", getParentGroup().getId());
		return json;
	}
	
}
