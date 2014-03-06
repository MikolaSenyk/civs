/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Assistance entity
 *
 * @author mikola
 */
@Entity
@Table(name = "assistances")
@NamedQueries({
	@NamedQuery(
			name = "Assistances.findByGroupId",
			query = "SELECT a FROM Assistance a WHERE a.group.id = :groupId"),
	@NamedQuery(
			name = "Assistances.findByUserId",
			query = "SELECT a FROM Assistance a WHERE a.user.id = :userId ORDER BY createTime DESC")
})
public class Assistance implements Serializable {

	private static final long serialVersionUID = 1L;
	// fields
	@Id
	@Column(name = "id")
	@GeneratedValue
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false)
	private Date createTime;
	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
	@ManyToOne
    @JoinColumn(name = "group_id")
    private AssistanceGroup group;
	@Column(name = "description", nullable = false, length = 1024)
	private String description;
	@Column(name = "approved", nullable = false, columnDefinition = "bit")
    private boolean approved;

	public Assistance() {
		this.id = 0L;
		this.createTime = new Date(); // FIXME gmt+0
	}

	public Assistance(User user, AssistanceGroup group, String description) {
		this();
		this.user = user;
		this.group = group;
		this.description = description;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AssistanceGroup getGroup() {
		return group;
	}

	public void setGroup(AssistanceGroup group) {
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
