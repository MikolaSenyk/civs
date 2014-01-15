/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.model.dto;

/**
 * Data Transfer Object for RegOption
 * @author mikola
 */
public class RegOptionDto extends MessageDto {
	
	private long id;
    private String code;

	public RegOptionDto(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
