/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity of user
 * @author mikola
 */
@Entity
@Table(name = "users")
@NamedQueries({
	@NamedQuery(
		name = "Users.findByLogin",
		query = "SELECT u FROM User u WHERE u.login = :login"
	),
	@NamedQuery(
		name = "Users.doLogin",
		query = "SELECT u FROM User u WHERE u.login = :login AND u.passwd = :passwd AND u.enabled = TRUE"
	),
	@NamedQuery(
		name = "Users.findAllUsers",
		query = "SELECT u FROM User u"
	)
})
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // fields
    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false)
	private Date createTime;
	@Column(name = "login", nullable = false, length = 32)
    private String login;
	@Column(name = "passwd", nullable = false, length = 32)
    private String passwd;
	@Enumerated(EnumType.STRING)
    @Column(name = "`role`", nullable = false)
    private UserRole role;
	@Column(name = "enabled", nullable = false, columnDefinition = "bit")
    private boolean enabled;

	public User() {
		this.id = 0L;
		this.createTime = new Date(); // FIXME use GMT+0
	}

	public User(String login, String passwd) {
		this();
		this.login = login;
		this.passwd = passwd;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
