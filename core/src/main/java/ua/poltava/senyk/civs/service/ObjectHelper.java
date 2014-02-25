/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.service;

import ua.poltava.senyk.civs.model.AssistanceGroup;
import ua.poltava.senyk.civs.model.Task;
import ua.poltava.senyk.civs.model.User;
import ua.poltava.senyk.civs.model.dto.AssistanceGroupDto;
import ua.poltava.senyk.civs.model.dto.TaskDto;
import ua.poltava.senyk.civs.model.dto.UserDto;


/**
 * Useful stuff for creating Dto from Entity
 * @author mikola
 */
public class ObjectHelper {
	
	public UserDto getUser(User user) {
		UserDto o = new UserDto();
		if ( user != null ) {
			o.setId(user.getId());
			o.setLogin(user.getLogin());
			o.setPasswd(user.getPasswd());
			o.setRole(user.getRole());
			o.setEnabled(user.isEnabled());
			o.setSuccess(true);
		}
		return o;
	}
	
	public TaskDto getTask(Task task) {
		TaskDto o = new TaskDto();
		if ( task != null ) {
			o.setId(task.getId());
			o.setUser(getUser(task.getUser()));
			o.setCreateTime(task.getCreateTime());
			o.setPriority(task.getPriority());
			o.setTitle(task.getTitle());
			o.setDescription(task.getDescription());
			o.setStatus(task.getStatus());
			o.setSuccess(true);
		}
		return o;
	}
	
	public AssistanceGroupDto getAssistanceGroup(AssistanceGroup group) {
		AssistanceGroupDto o = new AssistanceGroupDto();
		if ( group != null ) {
			o.setId(group.getId());
			o.setName(group.getName());
			o.setReadOnly(group.isReadOnly());
		}
		return o;
	}
    
}
