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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Group of assistance entity
 * @author mikola
 */
@Entity
@Table(name = "assistance_groups")
@NamedQueries({
	@NamedQuery(
		name = "AssistanceGroups.findAll",
		query = "SELECT ag FROM AssistanceGroup ag ORDER BY ag.name"
	),
    @NamedQuery(
		name = "AssistanceGroups.findAllParent",
		query = "SELECT ag FROM AssistanceGroup ag WHERE ag.parentGroup.id = :parentId ORDER BY ag.name"
	),
    @NamedQuery(
		name = "AssistanceGroups.findTop",
		query = "SELECT ag FROM AssistanceGroup ag WHERE ag.level = 1 ORDER BY ag.name"
	)
})
public class AssistanceGroup implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // fields
    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;
	@Column(name = "name", nullable = false, unique = true, length = 32)
    private String name;
    @Column(name = "`level`")
    private int level;
	@ManyToOne
    @JoinColumn(name = "parent_id")
    private AssistanceGroup parentGroup;

	public AssistanceGroup() {
		this.id = 0L;
	}
	
	public AssistanceGroup(String name) {
		this();
		this.name = name;
        this.level = 1;
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

    public AssistanceGroup getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(AssistanceGroup parentGroup) {
        this.parentGroup = parentGroup;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    
}
