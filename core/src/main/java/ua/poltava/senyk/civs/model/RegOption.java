/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity of registration's options
 * Not sure if it object is needed
 * @author mikola
 */
@Entity
@Table(name = "reg_options")
public class RegOption implements Serializable {
	
	public static final long PRIMARY_REG_OPTION_ID = 1L;
	
	// fields
    @Id
    @Column(name = "id")
    private long id;
	@Column(name = "code", nullable = false, length = 5)
    private String code;

	public RegOption() {
		this.id = 0L;
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
