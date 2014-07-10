/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.poltava.senyk.civs.model.dto;

import java.io.File;
import java.util.Date;
import net.sf.json.JSONObject;

/**
 * Data Transfer Object for UploadedImage entity
 * @author Msenyk
 */
public class UploadedImageDto extends MessageDto {
    
    // fields
    private long id;
	private Date createTime;
    private UserDto user;
    private String folder;
    private String extension;

    public UploadedImageDto() {
        this.id = 0L;
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

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
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

    @Override
    public JSONObject getJSON() {
        JSONObject json = super.getJSON();
        json.put("id", getId());
        json.put("path", getFolder() + File.separator + String.valueOf(this.getId()) + "." + getExtension());
        return json;
    }
    
}
