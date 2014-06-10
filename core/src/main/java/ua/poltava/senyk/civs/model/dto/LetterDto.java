/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.poltava.senyk.civs.model.dto;

import java.util.Date;
import net.sf.json.JSONObject;
import ua.poltava.senyk.civs.utils.DatetimeFormat;

/**
 * Data Transfer Object for Letter entity
 * @author Msenyk
 */
public class LetterDto extends MessageDto {
	
	private long id;
	private Date createTime;
    private UserDto fromUser;
    private UserDto toUser;
    private String message;
    private boolean read;

    public LetterDto() {
        this.id = -1L;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject json = super.getJSON();
        json.put("id", getId());
        json.put("createTime", DatetimeFormat.getDate(getCreateTime()));
		json.put("fromUser", getFromUser().getJSON());
        json.put("toUserId", fromUser.getId());
        json.put("message", getMessage());
        json.put("read", isRead());
        return json;
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

    public UserDto getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserDto fromUser) {
        this.fromUser = fromUser;
    }

    public UserDto getToUser() {
        return toUser;
    }

    public void setToUser(UserDto toUser) {
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
