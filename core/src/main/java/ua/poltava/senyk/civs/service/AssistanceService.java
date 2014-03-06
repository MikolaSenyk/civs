/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.poltava.senyk.civs.dao.AssistanceDao;
import ua.poltava.senyk.civs.dao.AssistanceGroupDao;
import ua.poltava.senyk.civs.model.Assistance;
import ua.poltava.senyk.civs.model.AssistanceGroup;
import ua.poltava.senyk.civs.model.dto.AssistanceDto;
import ua.poltava.senyk.civs.model.dto.AssistanceGroupDto;

/**
 * Service for assistances and groups
 * @author mikola
 */
public class AssistanceService {
	
	private static Logger logger = Logger.getLogger(AssistanceService.class);
	
	@Autowired
	private AssistanceGroupDao _groupDao;
	@Autowired
	private AssistanceDao _assistanceDao;
	
	// Assistance Groups
	
	@Transactional(rollbackFor = Exception.class)
	public List<AssistanceGroupDto> findGroups() throws Exception {
		List<AssistanceGroup> groups = _groupDao.findGroups();
		ObjectHelper helper = new ObjectHelper();
		List<AssistanceGroupDto> dtoGroups = new ArrayList<AssistanceGroupDto>(groups.size());
		for (AssistanceGroup group : groups) {
			dtoGroups.add(helper.getAssistanceGroup(group));
		}
		return dtoGroups;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public AssistanceGroupDto findGroup(long id) throws Exception {
		ObjectHelper helper = new ObjectHelper();
		return helper.getAssistanceGroup(_groupDao.getById(id));
	}
	
	@Transactional(rollbackFor = Exception.class)
	public AssistanceGroupDto createGroup(String name) throws Exception {
		ObjectHelper helper = new ObjectHelper();
		return helper.getAssistanceGroup(_groupDao.addGroup(name));
	}
	
	@Transactional(rollbackFor = Exception.class)
	public AssistanceGroupDto updateGroup(long id, String name) throws Exception {
		ObjectHelper helper = new ObjectHelper();
		return helper.getAssistanceGroup(_groupDao.updateGroup(id, name));
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void removeGroup(long id) throws Exception {
		// check existing assistance
		if ( _assistanceDao.isAnyAssistanceByGroupId(id) )
			throw new Exception("Group isn't empty");
		_groupDao.removeGroup(id);
	}
	
	// Assistances
	
	@Transactional(rollbackFor = Exception.class)
	public List<AssistanceDto> findUserAssistances(long userId) throws Exception {
		List<AssistanceDto> userAssistances = new ArrayList<AssistanceDto>();
		ObjectHelper helper = new ObjectHelper();
		for (Assistance assistance: _assistanceDao.findUserAssistances(userId)) {
			userAssistances.add(helper.getAssistance(assistance));
		}
		return userAssistances;
	}
	
}
