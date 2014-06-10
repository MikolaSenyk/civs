/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * Letter entity (message)
 * @author Msenyk
 */
@Entity
@Table(name = "letters")
@NamedQueries({
	@NamedQuery(
        name = "Letters.findNewToUser",
        query = "SELECT m FROM Letter m WHERE m.toUser.id = :userId AND m.read = 0 ORDER BY m.createTime")
})
public class Letter  implements Serializable {
    
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
    @JoinColumn(name = "from_user_id")
    private User fromUser;
    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;
	@Column(name = "message", nullable = false, length = 512)
    private String message;
    @Column(name = "`read`", nullable = false, columnDefinition = "bit")
    private boolean read;

    public Letter() {
        this.id = 0L;
        this.createTime = new Date(); // FIXME use GMT zone
        this.read = false;
    }

    public Letter(User fromUser, User toUser, String message) {
        this();
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.message = message;
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

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
    
}
