/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
			name = "Assistances.findAll",
			query = "SELECT a FROM Assistance a ORDER BY a.createTime DESC"),
    @NamedQuery(
			name = "Assistances.findAllEnabled",
			query = "SELECT a FROM Assistance a WHERE a.enabled = 1 ORDER BY a.createTime DESC"),
    @NamedQuery(
			name = "Assistances.findNew",
			query = "SELECT a FROM Assistance a WHERE a.approved <> 1 ORDER BY a.createTime DESC"),
    @NamedQuery(
			name = "Assistances.findByGroupId",
			query = "SELECT agl.assistance FROM AssistanceGroupLink agl WHERE agl.group.id = :groupId ORDER BY agl.assistance.createTime DESC"),
	@NamedQuery(
			name = "Assistances.findByUserId",
			query = "SELECT a FROM Assistance a WHERE a.user.id = :userId ORDER BY a.createTime DESC")
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
	@OneToMany(mappedBy="assistance")
    private List<AssistanceGroupLink> groupLinks;
	@Column(name = "description", nullable = false, length = 1024)
	private String description;
	@Column(name = "approved", nullable = false, columnDefinition = "bit")
    private boolean approved;
    @Column(name = "enabled", nullable = false, columnDefinition = "bit")
    private boolean enabled;

	public Assistance() {
		this.id = 0L;
		this.createTime = new Date(); // FIXME gmt+0
        this.enabled = true;
	}

	public Assistance(User user, String description) {
		this();
		this.user = user;
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

    public List<AssistanceGroupLink> getGroupLinks() {
        return groupLinks;
    }

    public void setGroupLinks(List<AssistanceGroupLink> groupLinks) {
        this.groupLinks = groupLinks;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
	
}
