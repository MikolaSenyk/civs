/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.persistence.Table;

/**
 * Entity of link between assistance and group
 * @author Msenyk
 */
@Entity
@Table(name = "assistance_group_links")
public class AssistanceGroupLink implements Serializable {

	private static final long serialVersionUID = 1L;
	// fields
	@Id
	@Column(name = "id")
	@GeneratedValue
	private long id;
    @ManyToOne
    @JoinColumn(name = "assistance_id")
    private Assistance assistance;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private AssistanceGroup group;

    public AssistanceGroupLink() {
        this.id = 0L;
    }

    public AssistanceGroupLink(Assistance assistance, AssistanceGroup group) {
        this();
        this.assistance = assistance;
        this.group = group;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Assistance getAssistance() {
        return assistance;
    }

    public void setAssistance(Assistance assistance) {
        this.assistance = assistance;
    }

    public AssistanceGroup getGroup() {
        return group;
    }

    public void setGroup(AssistanceGroup group) {
        this.group = group;
    }
    
}
