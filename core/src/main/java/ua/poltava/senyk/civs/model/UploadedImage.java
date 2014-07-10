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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity of UploadedImage
 * @author Msenyk
 */
@Entity
@Table(name = "images")
public class UploadedImage  implements Serializable {
    
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
	@Column(name = "folder", nullable = false, length = 64)
    private String folder;
    @Column(name = "ext", nullable = false, length = 3)
    private String extension;

    public UploadedImage(User user, String folder, String ext) {
        this.user = user;
        this.folder = folder;
        this.extension = ext;
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

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

}
