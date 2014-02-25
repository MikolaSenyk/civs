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
import ua.poltava.senyk.civs.dao.AssistanceGroupDao;
import ua.poltava.senyk.civs.model.AssistanceGroup;
import ua.poltava.senyk.civs.model.dto.AssistanceGroupDto;

/**
 * Service for assistances and groups
 * @author mikola
 */
public class AssistanceService {
	
	private static Logger logger = Logger.getLogger(AssistanceService.class);
	
	@Autowired
	private AssistanceGroupDao _groupDao;
	
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
	
}
