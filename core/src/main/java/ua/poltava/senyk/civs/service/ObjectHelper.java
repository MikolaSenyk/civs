/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.service;

import net.sf.json.JSONObject;
import ua.poltava.senyk.civs.model.Assistance;
import ua.poltava.senyk.civs.model.AssistanceGroup;
import ua.poltava.senyk.civs.model.Letter;
import ua.poltava.senyk.civs.model.UploadedImage;
import ua.poltava.senyk.civs.model.User;
import ua.poltava.senyk.civs.model.dto.AssistanceDto;
import ua.poltava.senyk.civs.model.dto.AssistanceGroupDto;
import ua.poltava.senyk.civs.model.dto.LetterDto;
import ua.poltava.senyk.civs.model.dto.UploadedImageDto;
import ua.poltava.senyk.civs.model.dto.UserDto;
import ua.poltava.senyk.civs.utils.JsonHelper;


/**
 * Useful stuff for creating Dto from Entity
 * @author mikola
 */
public class ObjectHelper {
	
	public UserDto getUser(User user) {
		UserDto o = new UserDto();
		if ( user != null ) {
			updateUserOptions(o, user.getOptions());
			o.setId(user.getId());
			o.setLogin(user.getLogin());
			o.setPasswd(user.getPasswd());
			o.setRole(user.getRole());
			o.setEnabled(user.isEnabled());
            o.setCreateTime(user.getCreateTime());
			o.setSuccess(true);
		}
		return o;
	}
	
	public void updateUserOptions(UserDto o, String body) {
		JSONObject optionsJson = JSONObject.fromObject(body);
		if ( optionsJson.containsKey("firstName") ) o.setFirstName(optionsJson.getString("firstName"));
		if ( optionsJson.containsKey("lastName") ) o.setLastName(optionsJson.getString("lastName"));
		if ( optionsJson.containsKey("middleName") ) o.setMiddleName(optionsJson.getString("middleName"));
        if ( optionsJson.containsKey("avatarPath") ) o.setAvatarPath(optionsJson.getString("avatarPath"));
        // required fields
        if ( JsonHelper.hasRequiredStringField("phone", optionsJson) ) {
            o.setPhone(optionsJson.getString("phone"));
        } else {
            o.setSuccess(false);
            o.setMessageText("Phone is requred");
            return;
        }
        if ( JsonHelper.hasRequiredStringField("address", optionsJson) ) {
            o.setAddress(optionsJson.getString("address"));
        } else {
            o.setSuccess(false);
            o.setMessageText("Address is requred");
            return;
        }
	}
		
	public AssistanceGroupDto getAssistanceGroup(AssistanceGroup group) {
		AssistanceGroupDto o = new AssistanceGroupDto();
		if ( group != null ) {
			o.setId(group.getId());
			o.setName(group.getName());
			o.setReadOnly(group.isReadOnly());
			o.setSuccess(true);
		}
		return o;
	}
	
	public AssistanceDto getAssistance(Assistance assistance) {
		AssistanceDto o = new AssistanceDto();
		if ( assistance != null ) {
			o.setId(assistance.getId());
			o.setCreateTime(assistance.getCreateTime());
			o.setDescription(assistance.getDescription());
			o.setApproved(assistance.isApproved());
            o.setEnabled(assistance.isEnabled());
			o.setGroup(getAssistanceGroup(assistance.getGroup()));
			o.setUser(getUser(assistance.getUser()));
			o.setSuccess(o.getUser().isSuccess() && o.getGroup().isSuccess());
		}
		return o;
	}
    
    public LetterDto getLetter(Letter letter) {
        LetterDto o = new LetterDto();
        if ( letter != null ) {
            o.setId(letter.getId());
            o.setCreateTime(letter.getCreateTime());
            o.setFromUser(getUser(letter.getFromUser()));
            o.setToUser(getUser(letter.getToUser()));
            o.setMessage(letter.getMessage());
            o.setRead(letter.isRead());
            o.setSuccess(true);
        }
        return o;
    }
    
    public UploadedImageDto getUploadedImage(UploadedImage img) {
        UploadedImageDto o = new UploadedImageDto();
        if ( img != null ) {
            o.setId(img.getId());
            o.setCreateTime(img.getCreateTime());
            o.setUser(getUser(img.getUser()));
            o.setFolder(img.getFolder());
            o.setExtension(img.getExtension());
            o.setSuccess(true);
        }
        return o;
    }
    
}
